package com.scms.scms_be.controller.General;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.UserDto;
import com.scms.scms_be.model.entity.General.User;
import com.scms.scms_be.service.General.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/sysad/get-all-users")
    public ResponseEntity<UserDto> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/comad/get-user-by-employeeId/{employeeId}")
    public ResponseEntity<UserDto> getUserByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(userService.getUserByEmployeeId(employeeId));
    }
    
    @GetMapping("/comad/get-user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody User newUser) {
        return ResponseEntity.ok(userService.updateUser(userId, newUser));
    }


    @GetMapping("/get-profile")
    public ResponseEntity<UserDto> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserDto response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
