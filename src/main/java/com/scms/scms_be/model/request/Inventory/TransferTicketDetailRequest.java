package com.scms.scms_be.model.request.Inventory;

import lombok.Data;

@Data
public class TransferTicketDetailRequest {

    private Long item_id;

    private Double quantity;

    private String note;
}
