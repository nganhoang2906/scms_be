package com.scms.scms_be.service.General;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.config.JWTUntils;
import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.model.entity.General.Employee;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.model.request.Auth.LoginRequest;
import com.scms.scms_be.model.request.Auth.RegisterComanyRequest;
import com.scms.scms_be.model.request.Auth.ResetPasswordRequest;
import com.scms.scms_be.model.request.Auth.VerifyOtpRequest;
import com.scms.scms_be.model.response.LoginResponse;
import com.scms.scms_be.repository.General.CompanyRepository;
import com.scms.scms_be.repository.General.DepartmentRepository;
import com.scms.scms_be.repository.General.EmployeeRepository;
import com.scms.scms_be.repository.General.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository usersRepo;

    @Autowired
    private JWTUntils jwtUntils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    public void registerCompany(RegisterComanyRequest registrationRequest) {
        if (usersRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new CustomException("Email đã được đăng ký.", HttpStatus.BAD_REQUEST);
        }

        Optional<Company> existingCompany = companyRepo.findByTaxCode(registrationRequest.getTaxCode());
        if (existingCompany.isPresent()) {
            throw new CustomException("Công ty đã tồn tại.", HttpStatus.BAD_REQUEST);
        }

        Company company = new Company();
        company.setCompanyName(registrationRequest.getCompanyName());
        company.setCompanyCode(registrationRequest.getCompanyCode());
        company.setTaxCode(registrationRequest.getTaxCode());
        company.setAddress(registrationRequest.getAddress());
        company.setCountry(registrationRequest.getCountry());
        company.setCompanyType(registrationRequest.getCompanyType());
        company.setMainIndustry(registrationRequest.getMainIndustry());
        company.setRepresentativeName(registrationRequest.getRepresentativeName());
        company.setJoinDate(new Date());
        company.setPhoneNumber(registrationRequest.getPhoneNumber());
        company.setEmail(registrationRequest.getEmail());
        company.setStatus("Đang hoat động");
        company = companyRepo.save(company);

        List<String> departmentNames = new ArrayList<>(Arrays.asList("Quản trị", "Kho", "Vận chuyển", "Mua hàng", "Bán hàng"));
        if (company.getCompanyType().equalsIgnoreCase("Doanh nghiệp sản xuất")) {
            departmentNames.add("Sản xuất");
        }

        int departmentIndex = departmentRepo.countByCompanyCompanyId(company.getCompanyId()) + 1;

        List<Department> departments = new ArrayList<>();
        for (String departmentName : departmentNames) {
            Department department = new Department();
            department.setCompany(company);
            department.setDepartmentCode(company.getCompanyCode() + "-D" + departmentIndex);
            department.setDepartmentName(departmentName);
            department = departmentRepo.save(department);
            departments.add(department);
            departmentIndex++;
        }

        Department managementDepartment = departments.stream()
                .filter(d -> d.getDepartmentName().equals("Quản trị"))
                .findFirst()
                .orElseThrow(() -> new CustomException("Không tìm thấy bộ phận Quản trị!", HttpStatus.INTERNAL_SERVER_ERROR));

        Employee employee = new Employee();
        employee.setDepartment(managementDepartment);
        employee.setEmployeeCode(registrationRequest.getEmployeeCode());
        employee.setPosition("Quản lý");
        employee.setEmail(registrationRequest.getEmail());
        employee.setStatus("Đang hoạt động");
        employee = employeeRepo.save(employee);

        String otp = String.format("%06d", new Random().nextInt(999999));
        User newUser = new User(
                employee,
                registrationRequest.getEmail(),
                registrationRequest.getUsername(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                "C-ADMIN",
                otp,
                "Đang hoạt động",
                false
        );
        usersRepo.save(newUser);
        emailService.sendOtpToEmail(registrationRequest.getEmail(), otp);
        throw new CustomException("Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.", HttpStatus.OK);
    }

    public void verifyOtp(VerifyOtpRequest request) {
        User user = usersRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng.", HttpStatus.NOT_FOUND));
        if (!Objects.equals(user.getOtp(), request.getOtp())) {
            throw new CustomException("OTP không hợp lệ hoặc đã hết hạn.", HttpStatus.BAD_REQUEST);
        }
        user.setVerified(true);
        user.setOtp(null);
        usersRepo.save(user);
        throw new CustomException("Xác thực tài khoản thành công.", HttpStatus.OK);
    }

    public void sendVerifyOtp(String email) {
        User user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng." + email, HttpStatus.NOT_FOUND));
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtp(otp);
        usersRepo.save(user);
        emailService.sendOtpToEmail(email, otp);
        throw new CustomException("Mã OTP đã được gửi đến email của bạn.", HttpStatus.OK);
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = usersRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng.", HttpStatus.NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepo.save(user);
        throw new CustomException("Đặt lại mật khẩu thành công.", HttpStatus.OK);
    }

    public LoginResponse login(LoginRequest request) {
        User user = usersRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Tài khoản không tồn tại.", HttpStatus.NOT_FOUND));

        if (!user.isVerified()) {
            throw new CustomException("Tài khoản chưa được xác thực.", HttpStatus.FORBIDDEN);
        }
        if (!user.getStatus().equals("Đang hoạt động")) {
            throw new CustomException("Tài khoản đã bị khóa.", HttpStatus.FORBIDDEN);
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new CustomException("Mật khẩu không chính xác.", HttpStatus.UNAUTHORIZED);
        }

        String jwt = jwtUntils.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(jwt);
        response.setEmployeeId(user.getEmployee().getEmployeeId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        return response;
    }
}
