package com.scms.scms_be.model.request.Inventory;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReceiveTicketRequest {

    private Long companyId;

    private Long warehouseId;

    private LocalDateTime receiveDate;

    private String reason;
    private String receiveType; // mo/po/ticket
    private String referenceCode;

    private String createdBy;
    
    private String status;
    private String file;

    private String note;
}
