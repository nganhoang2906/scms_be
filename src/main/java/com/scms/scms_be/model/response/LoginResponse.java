package com.scms.scms_be.model.response;

import lombok.Data;

@Data
public class LoginResponse {
  private String token;
  private Long userId;
  private Long employeeId;
  private String employeeCode;
  private String username;
  private String email;
  private String role;
  private String status;
}
