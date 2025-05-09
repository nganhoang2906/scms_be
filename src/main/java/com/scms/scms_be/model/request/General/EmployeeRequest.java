package com.scms.scms_be.model.request.General;

import java.util.Date;

import lombok.Data;

@Data
public class EmployeeRequest {
  private String employeeCode;
  private String employeeName;
  private String position;
  private String gender;
  private String address;
  private String email;
  private String phoneNumber;
  private Date dateOfBirth;
  private Date employmentStartDate;
  private String status;
  private Long departmentId;
  private String username;
  private String password;
  private String role;
}
