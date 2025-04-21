package com.scms.scms_be.model.request.Purchasing;

import lombok.Data;

@Data
public class RfqDetailRequest {
    private Long itemId;
    private Double quantity;
    private String note;
}
