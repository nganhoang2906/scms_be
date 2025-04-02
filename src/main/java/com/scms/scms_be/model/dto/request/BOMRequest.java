package com.scms.scms_be.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BOMRequest {
    private Long itemId;
    private String description;
    private String status;
    private List<BOMDetailDTO> details;

    @Data
    public static class BOMDetailDTO {
        private Long id;
        private Long itemId;
        private Long quantity;
        private String note;
    }
}
