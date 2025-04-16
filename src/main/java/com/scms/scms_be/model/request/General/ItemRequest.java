package com.scms.scms_be.model.request.General;

import lombok.Data;

@Data
public class ItemRequest {
    private String itemCode;
    private String itemName;
    private String itemType;
    private String uom;
    private String technicalSpecifications;
    private Double importPrice;
    private Double exportPrice;
    private String description;
}
