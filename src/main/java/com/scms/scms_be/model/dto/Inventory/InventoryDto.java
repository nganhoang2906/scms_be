package com.scms.scms_be.model.dto.Inventory;

import lombok.Data;

@Data
public class InventoryDto {

    private Long inventoryId;

    private Long warehouseId;

    private Long itemId;

    private Double quantity;
    private Double onDemandQuantity;

}
