package com.scms.scms_be.model.request.Inventory;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class IssueTicketRequest {

    private Long companyId;

    private Long warehouseId;

    private LocalDateTime issueDate;

    private String reason;
    private String issueType; // mo/so/ticket

    private String referenceCode;


    private String createdBy;

    private String status;
    private String file;

    private List<IssueTicketDetailRequest> details;
}
