package com.scms.scms_be.controller.General;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.UserDto;
import com.scms.scms_be.model.request.Auth.LoginRequest;
import com.scms.scms_be.model.request.Auth.RegisterComanyRequest;
import com.scms.scms_be.model.request.Auth.ResetPasswordRequest;
import com.scms.scms_be.model.request.Auth.VerifyOtpRequest;
import com.scms.scms_be.model.response.LoginResponse;
import com.scms.scms_be.service.General.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
     
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @PostMapping("/auth/register-company")
        public ResponseEntity<Object> registerCompany(    @RequestBody RegisterComanyRequest request) {
        authService.registerCompany(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/verify-otp")
    public ResponseEntity<UserDto> verifyOtp(   @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/send-verify-otp")
    public ResponseEntity<UserDto> sendVerifyOtp(  @RequestParam("email") String email ) {
        authService.sendVerifyOtp(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/verify-otp-forgot-password")
    public ResponseEntity<UserDto> verifyForgotPasswordOtp( @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<UserDto> resetPassword(    @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(     @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

       @PostMapping("/auth/logout")    
    public ResponseEntity<Object> logout(   @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        redisTemplate.opsForValue().set("BLACKLISTED_TOKEN:" + token, "true", 10, TimeUnit.HOURS);
        throw new CustomException("Logout successful - token invalidated",HttpStatus.OK);
    }
}
