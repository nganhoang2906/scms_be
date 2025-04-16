package com.scms.scms_be.model.request.Inventory;

import lombok.Data;

@Data
public class putItemToInventoryRequest {

    private Long warehouseId;

    private Long itemId;

    private Double quantity;
}
