package com.scms.scms_be.model.request.Sales;

import java.util.List;

import lombok.Data;

@Data
public class SalesOrderRequest {
    private Long poId;

    private String description;
    private String createdBy;
    private String status;

    private List<SalesOrderDetailRequest> salesOrderDetails;
}
