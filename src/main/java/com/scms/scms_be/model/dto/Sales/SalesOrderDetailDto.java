package com.scms.scms_be.model.dto.Sales;

import lombok.Data;

@Data
public class SalesOrderDetailDto {
    private Long soDetailId;

    private Long soId;

    private Long itemId;
    private String itemCode;
    private String itemName;

    private Double quantity;
    private Double itemPrice;
    private String note;
}
