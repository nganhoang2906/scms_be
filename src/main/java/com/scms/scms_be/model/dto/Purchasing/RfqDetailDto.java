package com.scms.scms_be.model.dto.Purchasing;

import lombok.Data;

@Data
public class RfqDetailDto {
    private Long RfqDetailId;

    private Long rfqId;
    private String rfqCode;

    private Long itemId;
    private String itemCode;
    private String itemName; 
    
    private Double quantity;
    private String note;
}
