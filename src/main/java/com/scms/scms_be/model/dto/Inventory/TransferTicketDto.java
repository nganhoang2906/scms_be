package com.scms.scms_be.model.dto.Inventory;


import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TransferTicketDto {

    private Long ticketId;

    private Long companyId;

    private String ticketCode;

    private Long from_warehouseId;
    private String from_warehouseCode;
    private String from_warehouseName;

    private Long to_warehouseId;
    private String to_warehouseCode;
    private String to_warehouseName;

    private String reason;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;
    private String file;

    private List<TransferTicketDetailDto> transferTicketDetails;
}
