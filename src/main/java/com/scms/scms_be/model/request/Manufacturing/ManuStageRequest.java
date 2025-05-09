package com.scms.scms_be.model.request.Manufacturing;

import java.util.List;

import lombok.Data;

@Data
public class ManuStageRequest {
  private Long itemId;
  private String description;
  private String status;

  private List<ManuStageDetailRequest> stageDetails;
}
