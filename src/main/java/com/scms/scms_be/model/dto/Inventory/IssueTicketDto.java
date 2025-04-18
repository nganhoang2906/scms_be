package com.scms.scms_be.model.dto.Inventory;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class IssueTicketDto {
    private Long ticketId;

    private Long companyId;

    private String ticketCode;

    private Long warehouseId;
    private String warehouseCode;
    private String warehouseName;

    private LocalDateTime issueDate;
    private String reason;

    private String issueType; // mo/so/ticket
    private Long referenceId;
    
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;
    private String file;

    private List<IssueTicketDetailDto> issueTicketDetails;
}
