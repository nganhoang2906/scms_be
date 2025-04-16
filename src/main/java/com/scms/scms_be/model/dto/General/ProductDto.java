package com.scms.scms_be.model.dto.General;

import lombok.Data;

@Data
public class ProductDto {
    private Long productId;
    private Long itemId;
    private Long currentCompanyId;
    private String serialNumber;
    private Long batchId;
    private String qrCode;

}
