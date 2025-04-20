package com.scms.scms_be.model.dto.Sales;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class SalesOrderDto {
    private Long soId;
    private String soCode;

    private Long companyId;
    private String companyName;

    private Long poId;
    private String poCode;

    private String description;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;

    private List<SalesOrderDetailDto> salesOrderDetails;
}
