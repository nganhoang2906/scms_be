package com.scms.scms_be.model.request.Manufacturing;

import java.util.Date;

import lombok.Data;

@Data
public class ManuOrderRequest {
    private String type;
    private Integer quantity;

    private Date estimatedStartTime;
    private Date estimatedEndTime;

    private String createdBy;

    private String status;
}
