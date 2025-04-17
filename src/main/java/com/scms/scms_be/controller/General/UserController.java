package com.scms.scms_be.controller.General;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scms.scms_be.model.dto.General.UserDto;
import com.scms.scms_be.model.request.Auth.UpdatePasswordRequest;
import com.scms.scms_be.model.request.General.UpdateInfoRequest;
import com.scms.scms_be.service.General.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/sysad/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/comsys/get-all-users-in-com/{companyId}")
    public ResponseEntity<List<UserDto>> getAllUsersInCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(userService.getAllUsersByCompanyId(companyId));
    }

    @GetMapping("/user/get-user-by-employeeId/{employeeId}")
    public ResponseEntity<UserDto> getUserByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(userService.getUserByEmployeeId(employeeId));
    }

    @GetMapping("/user/get-user-by-userId/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/all/update-user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UpdateInfoRequest newUser) {
        return ResponseEntity.ok(userService.updateUser(userId, newUser));
    }

    @PostMapping("/users/update-password/{userId}")
    public ResponseEntity<String> updatePassword(
            @PathVariable Long userId,
            @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(userId, request);
        return ResponseEntity.ok("Cập nhật mật khẩu thành công");
    }

}
