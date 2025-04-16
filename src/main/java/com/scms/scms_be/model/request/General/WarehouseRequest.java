package com.scms.scms_be.model.request.General;

import lombok.Data;

@Data
public class WarehouseRequest {
    private String warehouseCode;
    private String warehouseName;
    private String description;
    private double maxCapacity;
    private String warehouseType;
    private String status;
}
