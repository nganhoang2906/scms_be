package com.scms.scms_be.model.request.Auth;

import java.util.Date;

import lombok.Data;

@Data
public class RegisterCompanyRequest {
  // Thông tin công ty
  private String companyName;
  private String taxCode;
  private String address;
  private String country;
  private String companyType;
  private String mainIndustry;
  private String representativeName;
  private Date startDate;
  private String phoneNumber;
  private String email;
  private String websiteAddress;

  // Thông tin nhân viên
  private String employeeCode;
  private String position;
  private String password;
}
