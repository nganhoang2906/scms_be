package com.scms.scms_be.model.request.General;

import java.util.Date;

import lombok.Data;

@Data
public class CompanyRequest {
    private String companyCode;
    private String taxCode;
    private String companyName;
    private String address;
    private String country;
    private String companyType;
    private String mainIndustry;
    private String representativeName;
    private Date startDate;
    private String phoneNumber;
    private String email;
    private String websiteAddress;
    
    private String logoUrl;
    private String status;
}
