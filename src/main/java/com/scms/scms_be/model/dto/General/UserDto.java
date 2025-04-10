package com.scms.scms_be.model.dto.General;

import java.util.List;

import com.scms.scms_be.model.entity.General.User;

import lombok.Data;

@Data
public class UserDto {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;

    private Long userId;
    private Long employeeId;
    private String employeeCode;
    private String username;
    private String email;
    private String role;
    private String status;
    private String otp;
    private User ourUser;
    
    private List<UserDto> ourUsersList;

}
