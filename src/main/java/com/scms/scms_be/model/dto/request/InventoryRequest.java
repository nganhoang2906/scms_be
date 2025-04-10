package com.scms.scms_be.model.dto.request;

import lombok.Data;

@Data
public class InventoryRequest {
    private Long warehouseId;

    private Long itemId;

    private Double quantity;
    private Double onDemandQuantity;
    
}
