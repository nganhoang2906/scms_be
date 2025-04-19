package com.scms.scms_be.model.dto.General;


import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private Long employeeId;
    private String employeeCode;
    private String username;
    private String email;
    private String role;
    private String status;
}
