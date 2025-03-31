package com.scms.scms_be.service.General;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scms.scms_be.config.JWTUntils;
import com.scms.scms_be.model.dto.UserDto;
import com.scms.scms_be.model.dto.request.LoginRequest;
import com.scms.scms_be.model.dto.request.RefreshTokenRequest;
import com.scms.scms_be.model.dto.request.RegisterComanyRequest;
import com.scms.scms_be.model.dto.request.ResetPasswordRequest;
import com.scms.scms_be.model.dto.request.VerifyOtpRequest;
import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.General.Department;
import com.scms.scms_be.model.entity.General.Employee;
import com.scms.scms_be.model.entity.General.User;
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

    
    public UserDto registerCompany(RegisterComanyRequest registrationRequest) {
        UserDto resp = new UserDto();
        Company company;
    
        try {
            // Kiểm tra xem email đã được đăng ký chưa
            if (usersRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email đã được đăng ký.");
                return resp;
            }
    
            // Kiểm tra nếu công ty đã tồn tại bằng mã số thuế (taxCode)
            Optional<Company> existingCompany = companyRepo.findByTaxCode(registrationRequest.getTaxCode());
    
            String role;
    
            if (existingCompany.isPresent()) {
                resp.setStatusCode(500);
                resp.setMessage("Công ty đã tồn tại.");
                return resp;
            } else {
                // Nếu công ty chưa tồn tại, tạo mới và User sẽ là "C-ADMIN"
                company = new Company();
                company.setCompanyName(registrationRequest.getCompanyName());
                company.setCompanyCode(registrationRequest.getCompanyCode()); // Mã công ty duy nhất
                company.setTaxCode(registrationRequest.getTaxCode());
                company.setAddress(registrationRequest.getAddress());
                company.setCountry(registrationRequest.getCountry());
                company.setCompanyType(registrationRequest.getCompanyType());
                company.setMainIndustry(registrationRequest.getMainIndustry());
                company.setRepresentativeName(registrationRequest.getRepresentativeName());
                company.setJoinDate(registrationRequest.getJoinDate());
                company.setPhoneNumber(registrationRequest.getPhoneNumber());
                company.setEmail(registrationRequest.getEmail());
                company.setStatus("Active");
                company = companyRepo.save(company);
                role = "C-ADMIN"; // Người tạo công ty sẽ có quyền ADMIN
           

             
                //   Xác định danh sách `Department` dựa trên `companyType`
                List<String> departmentNames = new ArrayList<>(Arrays.asList("Ban quản lý", "Kho", "Vận chuyển", "Mua hàng", "Bán hàng"));
                if (company.getCompanyType().equalsIgnoreCase("Doanh nghiệp sản xuất")) {
                    departmentNames.add("Sản xuất"); // Nếu là doanh nghiệp sản xuất thì thêm bộ phận Sản xuất
                }

                //   Lấy số lượng `Department` hiện có của công ty để tránh trùng mã
                int departmentIndex = departmentRepo.countByCompanyCompanyId(company.getCompanyId()) + 1;

                //   Tạo `Department`
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

                //   Lấy `Department` của `Ban quản lý`
                Department managementDepartment = departments.stream()
                        .filter(d -> d.getDepartmentName().equals("Ban quản lý"))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban Ban quản lý!"));

                //   Tạo `Employee` với chức vụ `Quản lý`
                Employee employee = new Employee();
                employee.setDepartment(managementDepartment);
                employee.setEmployeeCode(registrationRequest.getEmployeeCode());
                employee.setPosition("Quản lý"); // Mặc định là quản lý
                employee.setEmail(registrationRequest.getEmail());
                employee.setStatus("Active");
                employee = employeeRepo.save(employee);

    
                // Tạo User
                String otp = String.format("%06d", new Random().nextInt(999999));
                User newUser = new User(
                        employee,
                        registrationRequest.getEmail(),
                        registrationRequest.getUsername(),
                        passwordEncoder.encode(registrationRequest.getPassword()),
                        role,
                        otp,
                        "Active",
                        false // Chưa xác thực
                );
                newUser.setEmployee(employee);
                User savedUser = usersRepo.save(newUser);
        
                // Gửi OTP qua email
                emailService.sendOtpToEmail(registrationRequest.getEmail(), otp);
    
                resp.setOurUsers(savedUser);
                resp.setStatusCode(200);
                resp.setMessage("Đăng ký thành công! Vui lòng kiểm tra email để nhập OTP.");
        }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
    
    // Verify OTP
    public UserDto verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(verifyOtpRequest.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getOtp() != null && user.getOtp().equals(verifyOtpRequest.getOtp())) {
                    user.setVerified(true);
                    user.setOtp(null); // Clear OTP after verification
                    usersRepo.save(user);
                    resp.setStatusCode(200);
                    resp.setMessage("Xác thực OTP thành công. Bạn có thể đăng nhập ngay bây giờ.");
                } else {
                    resp.setStatusCode(400);
                    resp.setMessage("OTP không hợp lệ hoặc đã hết hạn.");
                }
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Không tìm thấy người dùng.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public UserDto sendVerifyOtp(String email) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                String otp = String.format("%06d", new Random().nextInt(999999));
                
                user.setOtp(otp);
                usersRepo.save(user);
                
                emailService.sendOtpToEmail(email, otp);
                
                resp.setStatusCode(200);
                resp.setMessage("OTP đã được gửi thành công.");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Không tìm thấy người dùng.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public UserDto sendForgotPasswordVerifyOtp(String email) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                String otp = String.format("%06d", new Random().nextInt(999999));
                
                user.setOtp(otp);
                usersRepo.save(user);
                
                emailService.sendOtpToEmail(email, otp);
                
                resp.setStatusCode(200);
                resp.setMessage("OTP đã được gửi thành công.");
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Không tìm thấy người dùng.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public UserDto verifyForgotPasswordOtp(VerifyOtpRequest verifyOtpRequest) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(verifyOtpRequest.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getOtp() != null && user.getOtp().equals(verifyOtpRequest.getOtp())) {
                    user.setVerified(true);
                    user.setOtp(null); 
                    usersRepo.save(user);
                    resp.setStatusCode(200);
                    resp.setMessage("Xác thực OTP thành công. Bạn có thể thay đổi mật khẩu ngay bây giờ.");
                } else {
                    resp.setStatusCode(400);
                    resp.setMessage("OTP không hợp lệ hoặc đã hết hạn.");
                }
            } else {
                resp.setStatusCode(404);
                resp.setMessage("Không tìm thấy người dùng.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public UserDto resetPassword(ResetPasswordRequest resetPasswordRequest) {
        UserDto resp = new UserDto();
        try{
            Optional<User> userOptional = usersRepo.findByEmail(resetPasswordRequest.getEmail());
            if(userOptional.isPresent()){
                User users = userOptional.get();
                users.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
                usersRepo.save(users);

                resp.setStatusCode(200);
                resp.setMessage("Đặt lại mật khẩu thành công.");
            }else{
                resp.setStatusCode(404);
                resp.setMessage("Không tìm thấy người dùng.");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public UserDto login(LoginRequest loginRequest) {
        UserDto response = new UserDto();
        try {

            User users = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

            //   Check if user is verified
            if (!users.isVerified()) {
                response.setStatusCode(403);
                response.setMessage("Tài khoản chưa được xác thực. Vui lòng kiểm tra email để nhập OTP.");
                return response;
            }

            if (!users.getStatus().equals("Active")) {
                response.setStatusCode(403);
                response.setMessage("Tài khoản đã bị khóa.");
                return response;
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUntils.generateToken(user);
            var refreshToken = jwtUntils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setRole(user.getRole());
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public UserDto refreshToken(RefreshTokenRequest  refreshTokenRequest) {
        UserDto response = new UserDto();
        try {
            String ourEmail = jwtUntils.extractUsername(refreshTokenRequest.getToken());
            User users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUntils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUntils.generateToken(users);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hrs");
                response.setMessage("Successfully Refresh Token ");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
            return response;
        }
    }
}
