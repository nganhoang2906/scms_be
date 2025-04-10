package com.scms.scms_be.model.dto.Manufacture;

import lombok.Data;

@Data
public class BOMDetailDto {
    private Long id;
    
    private Long bomId;

    private Long itemId;

    private Long quantity;
    private String note;
}
