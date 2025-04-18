package com.scms.scms_be.model.dto.Inventory;

import lombok.Data;

@Data
public class InventoryDto {

    private Long inventoryId;

    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;

    private Long itemId;
    private String itemCode;
    private String itemName;

    private Double quantity;
    private Double onDemandQuantity;

}
