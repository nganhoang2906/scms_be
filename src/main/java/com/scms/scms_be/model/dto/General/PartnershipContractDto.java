package com.scms.scms_be.model.dto.General;

import java.util.Date;

import lombok.Data;

@Data
public class PartnershipContractDto {
    private Long contractId;
    private Long companyId;
    private Long partnerCompanyId;
    private String contractCode;
    private String contractNumber;
    private String contractType;
    private String contractContent;
    private String signatureA;
    private String signatureB;
    private Date signDate;
    private Date validDate;
    private String status;
    private byte[] file;

}
