package com.scms.scms_be.service;

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
import com.scms.scms_be.dto.UserDto;
import com.scms.scms_be.entity.User;
import com.scms.scms_be.repository.UserRepository;

@Service
public class UserService {

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

    public UserDto register(UserDto registrationRequest) {
        UserDto resp = new UserDto();
        try {
            // Check if user already exists
            if (usersRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email đã được đăng ký.");
                return resp;
            }

            // Generate OTP
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Create User
            User newUser = new User(
                    registrationRequest.getEmail(),
                    registrationRequest.getUsername(),
                    passwordEncoder.encode(registrationRequest.getPassword()),
                    registrationRequest.getRole(),
                    otp,
                    "Active",
                    false // Not verified yet
            );
            
            User savedUser = usersRepo.save(newUser);

            // Send OTP via Email
            emailService.sendOTPtoEmail(registrationRequest.getEmail(), otp);

            if (savedUser.getAccountId() > 0) {
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
      // ✅ Verify OTP
      public UserDto verifyOtp(String email, String otp) {
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getOtp() != null && user.getOtp().equals(otp)) {
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

    public UserDto verifyOtp_forgotPassword(String email, String otp){
        UserDto resp = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if(userOptional.isPresent()){
                User user = userOptional.get();
                if(user.getOtp() != null && user.getOtp().equals(otp)){
                    
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

    public UserDto reset_password(String email, String newPassword){
        UserDto resp = new UserDto();
        try{
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if(userOptional.isPresent()){
                User users = userOptional.get();
                users.setPassword(passwordEncoder.encode(newPassword));
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

    


    public UserDto login(UserDto loginRequest) {
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

    public UserDto refreshToken(UserDto refreshTokenRequest) {
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

    public UserDto getAllUsers() {
        UserDto response = new UserDto();
        try {
            List<User> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                response.setOurUsersList(result);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred " + e.getMessage());
            return response;
        }
    }

    public UserDto getUserById(long id) {
        UserDto response = new UserDto();
        try {
            User userbyId = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            response.setOurUsers(userbyId);
            response.setStatusCode(200);
            response.setMessage("Found user with id: " + id + " successfully");
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred " + e.getMessage());
            return response;
        }
    }

    public UserDto deleteUser(Long userid) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findById(userid);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userid);
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user for delete");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to delete user " + e.getMessage());
            return response;
        }
    }

    public UserDto updateUser(Long userid, User updateUser) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findById(userid);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setUsername(updateUser.getUsername());
                existingUser.setEmail(updateUser.getEmail());
                existingUser.setRole(updateUser.getRole());
                existingUser.setStatus(updateUser.getStatus());

            // Fix: Check if the password is not null and not empty
            if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }


                User saveOurUser = usersRepo.save(existingUser);
                response.setOurUsers(saveOurUser);
                response.setStatusCode(200);
                response.setMessage("User Update Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user for Update");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to update user " + e.getMessage());
            return response;
        }
    }

    public UserDto getMyInfo(String email) {
        UserDto response = new UserDto();
        try {
            Optional<User> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                response.setOurUsers(userOptional.get());
                response.setStatusCode(200);
                response.setMessage("Successful");
            } else {
                response.setStatusCode(404);
                response.setMessage("Not found user");
            }
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error to get user Info " + e.getMessage());
            return response;
        }
    }

}
