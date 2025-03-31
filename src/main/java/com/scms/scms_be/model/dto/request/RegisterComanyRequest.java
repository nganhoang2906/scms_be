package com.scms.scms_be.model.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class RegisterComanyRequest {
    // Thông tin công ty
    private String companyCode;           // Mã công ty (duy nhất)
    private String companyName;           // Tên công ty
    private String taxCode;               // Mã số thuế công ty
    private String address;               // Địa chỉ công ty
    private String country;               // Quốc gia
    private String companyType;           // Loại hình công ty
    private String mainIndustry;          // Ngành nghề chính
    private String representativeName;    // Người đại diện pháp lý
    private Date startDate;               // Ngày thành lập công ty
    private Date joinDate;                // Ngày tham gia hệ thống
    private String phoneNumber;           // Số điện thoại công ty
    private String email;                 // Email công ty
    private String websiteAddress;        // Website công ty

    // Thông tin nhân viên
    private String employeeCode;          // Mã nhân viên (duy nhất)
    private String username;              // Tên đầy đủ nhân viên (Người đăng ký)
    private String position;              // Chức vụ trong công ty
    private String password;              // Mật khẩu tài khoản
}
