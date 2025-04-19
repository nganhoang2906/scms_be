package com.scms.scms_be.model.request.Manufacturing;

import lombok.Data;

@Data
public class BOMDetailRequest {
    private Long itemId;

    private Double quantity;
    private String note;
}
