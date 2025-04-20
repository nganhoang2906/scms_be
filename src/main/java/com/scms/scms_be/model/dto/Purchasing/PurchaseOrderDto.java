package com.scms.scms_be.model.dto.Purchasing;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class PurchaseOrderDto {
    private Long poId;
    private String poCode;

    private Long companyId;
    private String companyName;
    private Long suplierCompanyId;
    private String suplierCompanyName;

    private String description;
    private String createdBy;
    private String status;

    private List<PurchaseOrderDetailDto> purchaseOrderDetails;
}
