package com.scms.scms_be.model.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeDto {
    private Long departmentId;
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String position;
    private String gender;
    private String address;
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private Date employmentStartDate;
    private byte[] avatar;
    private String status;

    private List<EmployeeDto> employeeList;
}
