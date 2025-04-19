package com.scms.scms_be.model.request.Manufacturing;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ManuProcessRequest {
    private LocalDateTime startedOn;
    private LocalDateTime finishedOn;
    private String status;
}
