package com.scms.scms_be.model.request.Purchasing;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class RequestForQuotationRequest {
    private Long companyId;
    private Long requestedCompanyId;
    private LocalDateTime needByDate;
    private String createdBy;
    private String status;

    private List<RfqDetailRequest> rfqDetails;
}
