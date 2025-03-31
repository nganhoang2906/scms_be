package com.scms.scms_be.controller.General;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.UserDto;
import com.scms.scms_be.model.dto.request.LoginRequest;
import com.scms.scms_be.model.dto.request.RefreshTokenRequest;
import com.scms.scms_be.model.dto.request.RegisterComanyRequest;
import com.scms.scms_be.model.dto.request.ResetPasswordRequest;
import com.scms.scms_be.model.dto.request.VerifyOtpRequest;
import com.scms.scms_be.service.General.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register-company")
    public ResponseEntity<UserDto> registerCompany(@RequestBody RegisterComanyRequest request) {
        return ResponseEntity.ok(authService.registerCompany(request));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<UserDto> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/send-verify-otp")
    public ResponseEntity<UserDto> sendVerifyOtp(@RequestBody String email ) {
        return ResponseEntity.ok(authService.sendVerifyOtp(email));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<UserDto> sendForgotPasswordVerifyOtp(@RequestBody String email ) {
        return ResponseEntity.ok(authService.sendForgotPasswordVerifyOtp(email));
    }

    @PostMapping("/verify-forgot-password-otp")
    public ResponseEntity<UserDto> verifyForgotPasswordOtp(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyForgotPasswordOtp(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserDto> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
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
