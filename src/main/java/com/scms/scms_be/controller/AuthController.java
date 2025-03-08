package com.scms.scms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.UserDto;
import com.scms.scms_be.model.dto.request.LoginRequest;
import com.scms.scms_be.model.dto.request.RefreshTokenRequest;
import com.scms.scms_be.model.dto.request.RegisterRequest;
import com.scms.scms_be.model.dto.request.ResetPasswordRequest;
import com.scms.scms_be.model.dto.request.VerifyOtpRequest;
import com.scms.scms_be.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/otp-register")
    public ResponseEntity<UserDto> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/send-otp-toEmail")
    public ResponseEntity<UserDto> resendVerifyOtp(@RequestBody String email ) {
        return ResponseEntity.ok(authService.send_Otp_toEmail(email));
    }

    @PostMapping("/verify_Otp_forgot-password")
    public ResponseEntity<UserDto> verifyOtp_forgorPassword(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp_forgotPassword(request));
    }

    @PostMapping("/resend-otp-forgot-password")
    public ResponseEntity<UserDto> reset_password(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.reset_password(request));
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserDto> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
