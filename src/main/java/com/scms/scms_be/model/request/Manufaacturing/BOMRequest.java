package com.scms.scms_be.model.request.Manufaacturing;

import lombok.Data;

import java.util.List;

@Data
public class BOMRequest {
    private Long itemId;
    private String description;
    private String status;

    private List<BOMDetailRequest> bomDetailList;
}
