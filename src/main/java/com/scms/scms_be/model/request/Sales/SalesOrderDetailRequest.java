package com.scms.scms_be.model.request.Sales;

import lombok.Data;

@Data
public class SalesOrderDetailRequest {
    private Long itemId;
    private Double quantity;
    private Double discountRate;
    private String note;
}
