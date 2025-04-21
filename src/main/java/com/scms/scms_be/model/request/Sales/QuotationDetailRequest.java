package com.scms.scms_be.model.request.Sales;

import lombok.Data;

@Data
public class QuotationDetailRequest {
    private Long itemId;
    private Double quantity;
    private Double itemPrice;
    private String note;
}
