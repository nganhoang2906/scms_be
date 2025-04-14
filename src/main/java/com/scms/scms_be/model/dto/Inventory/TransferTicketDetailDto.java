package com.scms.scms_be.model.dto.Inventory;


import lombok.Data;

@Data
public class TransferTicketDetailDto {
    private Long TTdetailId;
    
    private Long ticket_id;

    private Long item_id;

    private Double quantity;
    private String note;
}
