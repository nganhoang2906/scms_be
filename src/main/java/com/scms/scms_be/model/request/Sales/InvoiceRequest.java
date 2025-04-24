package com.scms.scms_be.model.request.Sales;

import lombok.Data;

@Data
public class InvoiceRequest {
    private Long soId;

    private String paymentMethod;
    private String status;
}

