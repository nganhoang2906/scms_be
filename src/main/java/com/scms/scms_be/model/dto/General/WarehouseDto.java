package com.scms.scms_be.model.dto.General;

import lombok.Data;

@Data
public class WarehouseDto {
  private Long warehouseId;
  private Long companyId;
  private String warehouseCode;
  private String warehouseName;
  private String description;
  private double maxCapacity;
  private String warehouseType;
  private String status;
}
