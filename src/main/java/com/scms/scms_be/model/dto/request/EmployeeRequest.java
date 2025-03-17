package com.scms.scms_be.model.dto.request;

import lombok.Data;
import java.util.Date;

@Data
public class EmployeeRequest {
    // Thông tin Employee
    private String employeeCode;
    private String employeeName;
    private String position;
    private String gender;
    private String identityNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private Date employmentStartDate;
    private String status;
    private Long departmentId; // Chỉ cần ID, không cần toàn bộ đối tượng Department

    // Thông tin User
    private String username;
    private String password;
}
