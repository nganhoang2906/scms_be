package com.scms.scms_be.model.dto.General;

import java.util.List;

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

    private List<WarehouseDto> warehouseList;
}
