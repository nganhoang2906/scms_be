package com.scms.scms_be.model.request.Delivery;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DeliveryProcessRequest {
    private Long deliveryProcessId;
    private Long doId;
    private String location;
    private LocalDateTime arrivalTime;
    private String note;
}
