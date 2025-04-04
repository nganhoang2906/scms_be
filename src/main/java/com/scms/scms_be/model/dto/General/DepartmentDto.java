package com.scms.scms_be.model.dto.General;

import java.util.List;

import lombok.Data;

@Data
public class DepartmentDto {
    private Long companyId;
    private Long departmentId;
    private String departmentCode;
    private String departmentName;

    private List<DepartmentDto> DepartmentList;
}
