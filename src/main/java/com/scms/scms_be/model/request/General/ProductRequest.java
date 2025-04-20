package com.scms.scms_be.model.request.General;

import lombok.Data;

@Data
public class ProductRequest {
    private Long currentCompanyId;

    private Long batchNo;
    private String qrCode;
}
