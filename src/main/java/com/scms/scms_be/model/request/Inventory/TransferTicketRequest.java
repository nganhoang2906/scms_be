package com.scms.scms_be.model.request.Inventory;

import java.util.List;

import lombok.Data;

@Data
public class TransferTicketRequest {
  private Long companyId;
  private Long fromWarehouseId;
  private Long toWarehouseId;
  private String reason;
  private String createdBy;
  private String status;
  private String file;

  private List<TransferTicketDetailRequest> transferTicketDetails;

}
