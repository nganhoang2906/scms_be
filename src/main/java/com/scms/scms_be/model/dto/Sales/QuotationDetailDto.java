package com.scms.scms_be.model.dto.Sales;

import lombok.Data;

@Data
public class QuotationDetailDto {
    private Long quotaionDetailId;
    private Long quotationId;

    private Long itemId;
    private String itemCode;
    private String itemName;
    
    private Double quantity;
    private Double itemPrice;
    private String note;
}
