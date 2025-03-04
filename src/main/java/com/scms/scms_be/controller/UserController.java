package com.scms.scms_be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.dto.UserDto;
import com.scms.scms_be.entity.User;
import com.scms.scms_be.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/auth/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto reg) {
        return ResponseEntity.ok(userService.register(reg));
    }


    @PostMapping("/auth/otp-register")
    public ResponseEntity<UserDto> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyOtp(email, otp));
    }

    @PostMapping("/auth/send-otp-toEmail")
    public ResponseEntity<UserDto> resendVerifyOtp(@RequestParam String email ) {
        return ResponseEntity.ok(userService.send_Otp_toEmail(email));
    }

    @PostMapping("/auth/verify_Otp_forgot-password")
    public ResponseEntity<UserDto> verifyOtp_forgorPassword(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(userService.verifyOtp_forgotPassword(email, otp));
    }

    @PostMapping("/auth/resend-otp-forgot-password")
    public ResponseEntity<UserDto> reset_password(@RequestParam String email , @RequestParam String newpassword) {
        return ResponseEntity.ok(userService.reset_password(email , newpassword));
    }
    
    
    @PostMapping("/auth/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto reg) {
        return ResponseEntity.ok(userService.login(reg));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserDto> refreshToken(@RequestBody UserDto reg) {
        return ResponseEntity.ok(userService.refreshToken(reg));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserDto> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody User newUser) {
        return ResponseEntity.ok(userService.updateUser(userId, newUser));
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<UserDto> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDto response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
