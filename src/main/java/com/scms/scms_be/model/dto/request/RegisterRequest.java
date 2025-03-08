package com.scms.scms_be.model.dto.request;

import java.util.Date;

import javax.validation.constraints.Email;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Email
    private String email;
    private String username;
    private String password;
    private String taxCode; // Bắt buộc nhập mã số thuế
    private String companyName; // Tên công ty (nếu chưa tồn tại)
    private String address;
    private String country;
    private String phoneNumber;
    private String companyCode;
    private String employeeCode;
    private String position;

}
