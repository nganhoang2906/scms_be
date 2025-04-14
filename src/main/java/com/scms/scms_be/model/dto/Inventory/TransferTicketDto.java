package com.scms.scms_be.model.dto.Inventory;


import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class TransferTicketDto {

    private Long ticketId;

    private Long company_id;

    private String ticketCode;

    private Long from_warehouse_id;

    private Long to_warehouse_id;

    private String reason;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;
    private String file;

    private List<TransferTicketDetailDto> transferTicketDetails;
}
