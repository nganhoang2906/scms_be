package com.scms.scms_be.model.request.Purchasing;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseOrderRequest {
    private Long companyId;
    private Long suplierCompanyId;
    private String description;
    private String createdBy;
    private String status;

    private List<PurchaseOrderDetailRequest> purchaseOrderDetails;
}
