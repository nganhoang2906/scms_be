package com.scms.scms_be.model.dto.request;


import lombok.Data;

@Data
public class DepartmentRequest {
    private String departmentCode;
    private String departmentName;
    private Long companyId;
}

