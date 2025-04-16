package com.scms.scms_be.model.request.Manufaacturing;

import lombok.Data;

@Data
public class BOMDetailRequest {
    private Long itemId;

    private Long quantity;
    private String note;
}
