package com.scms.scms_be.model.dto.General;

import lombok.Data;

@Data
public class DepartmentDto {
  private Long companyId;
  private Long departmentId;
  private String departmentCode;
  private String departmentName;
}
