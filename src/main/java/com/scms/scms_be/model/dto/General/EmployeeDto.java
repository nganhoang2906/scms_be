package com.scms.scms_be.model.dto.General;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeDto {
    private Long employeeId;
    private Long departmentId;
    private String departmentName;
    private String employeeCode;
    private String employeeName;
    private String position;
    private String gender;
    private String address;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String avatar;
    private String avatarUrl;
    private String status;

    private List<EmployeeDto> employeeList;
}
