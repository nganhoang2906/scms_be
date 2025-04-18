package com.scms.scms_be.model.request.Inventory;


import java.util.List;

import lombok.Data;

@Data
public class TransferTicketRequest {
    private Long company_id;

    private Long from_warehouse_id;

    private Long to_warehouse_id;

    private String reason;

    private String createdBy;

    private String status;

    private String file;

    private List<TransferTicketDetailRequest> transferTicketDetails;

}
