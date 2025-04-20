package com.scms.scms_be.model.dto.Purchasing;

import lombok.Data;

@Data
public class PurchaseOrderDetailDto {
    private Long purchaseOrderDetailId;
    private Long poId;
    private String poCode;

    private Long itemId;
    private String itemCode;
    private String itemName;

    private Double quantity;
    private String note;
}
