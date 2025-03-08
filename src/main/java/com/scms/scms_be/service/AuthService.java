package com.scms.scms_be.service;

import java.util.HashMap;
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
import com.scms.scms_be.model.dto.request.RegisterRequest;
import com.scms.scms_be.model.dto.request.ResetPasswordRequest;
import com.scms.scms_be.model.dto.request.VerifyOtpRequest;
import com.scms.scms_be.model.entity.Company;
import com.scms.scms_be.model.entity.Employee;
import com.scms.scms_be.model.entity.User;
import com.scms.scms_be.repository.CompanyRepository;
import com.scms.scms_be.repository.EmployeeRepository;
import com.scms.scms_be.repository.UserRepository;

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

    
    public UserDto register(RegisterRequest registrationRequest) {
    UserDto resp = new UserDto();
    Company company;
    try {
        // Kiểm tra xem email đã được đăng ký chưa
        if (usersRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
            resp.setStatusCode(400);
            resp.setMessage("Email đã được đăng ký.");
            return resp;
        }

        // Kiểm tra nếu công ty đã tồn tại bằng mã thuế (taxCode)
        Optional<Company> existingCompany = companyRepo.findByTaxCode(registrationRequest.getTaxCode());

        String role;
      

        if (existingCompany.isPresent()) {
            // Nếu công ty đã tồn tại, User sẽ là "USER"
            company = existingCompany.get();
            role = "USER";
        } else {
            // Nếu công ty chưa tồn tại, tạo mới và User sẽ là "COMPANY_ADMIN"
            company = new Company();
            company.setCompanyName(registrationRequest.getCompanyName());
            company.setCompanyCode(registrationRequest.getCompanyCode()); // Mã công ty duy nhất
            company.setTaxCode(registrationRequest.getTaxCode());
            company.setHeadOfficeAddress(registrationRequest.getAddress());
            company.setCountry(registrationRequest.getCountry());
            company.setPhoneNumber(registrationRequest.getPhoneNumber());
            company.setEmail(registrationRequest.getEmail());
            company.setStatus("Active");
            company = companyRepo.save(company);
            role = "C-ADMIN"; // Người tạo công ty sẽ có quyền ADMIN
        }

        // Tạo Employee cho User
        Employee employee = new Employee();
        employee.setCompanyId(company.getCompanyId());
        employee.setEmployeeCode(registrationRequest.getEmployeeCode()); // Mã nhân viên duy nhất
        employee.setEmployeeName(registrationRequest.getUsername());
        employee.setEmail(registrationRequest.getEmail());
        employee.setPhoneNumber(registrationRequest.getPhoneNumber());
        employee.setPosition(registrationRequest.getPosition());
        employee.setStatus("Active");
        employee = employeeRepo.save(employee);

        // Tạo User
        String otp = String.format("%06d", new Random().nextInt(999999));
        User newUser = new User(
                registrationRequest.getEmail(),
                registrationRequest.getUsername(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                role,
                otp,
                "Active",
                false // Chưa xác thực
        );
        newUser.setEmployeeId(employee.getEmployeeId());
        User savedUser = usersRepo.save(newUser);

        // Gửi OTP qua email
        emailService.sendOTPtoEmail(registrationRequest.getEmail(), otp);

        resp.setOurUsers(savedUser);
        resp.setStatusCode(200);
        resp.setMessage("Đăng ký thành công! Vui lòng kiểm tra email để nhập OTP.");
    } catch (Exception e) {
        resp.setStatusCode(500);
        resp.setError(e.getMessage());
    }
    return resp;
}

      // ✅ Verify OTP
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

    public UserDto send_Otp_toEmail(String email) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                
                String otp = String.format("%06d", new Random().nextInt(999999));
                
                user.setOtp(otp);
                usersRepo.save(user);
                
                emailService.sendOTPtoEmail(email, otp);
                
                resp.setStatusCode(200);
                resp.setMessage("OTP đã được gửi lại thành công.");
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

    public UserDto verifyOtp_forgotPassword(VerifyOtpRequest verifyOtpRequest) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(verifyOtpRequest.getEmail());
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(user.getOtp() != null && user.getOtp().equals(verifyOtpRequest.getOtp())){
                    
                    user.setOtp(null);
                    usersRepo.save(user);

                    resp.setStatusCode(200);
                    resp.setMessage("Xác thực OTP thành công. Bạn có thể đặt lại mật khẩu ngay bây giờ.");
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

    public UserDto reset_password(ResetPasswordRequest resetPasswordRequest) {
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

            // ✅ Check if user is verified
            if (!users.isVerified()) {
                response.setStatusCode(403);
                response.setMessage("Tài khoản chưa được xác thực. Vui lòng kiểm tra email để nhập OTP.");
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
