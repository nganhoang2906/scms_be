package com.scms.scms_be.model.dto.Purchasing;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class RequestForQuotationDto {
    private Long rfqId;
    private String rfqCode;

    private Long companyId;
    private String companyName;

    private Long requestedCompanyId;
    private String requestedCompanyName;

    private LocalDateTime needByDate;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;

    private List<RfqDetailDto> rfqDetails;
}
