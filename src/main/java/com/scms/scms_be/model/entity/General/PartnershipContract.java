package com.scms.scms_be.model.entity.General;



import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "partnership_contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnershipContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "partner_company_id")
    private Company partnerCompany;

    private String contractCode;
    private String contractNumber;
    private String contractType;
    private String contractContent;
    private String signatureA;
    private String signatureB;

    @Temporal(TemporalType.DATE)
    private Date signDate;

    @Temporal(TemporalType.DATE)
    private Date validDate;

    private String status;

    @Lob
    private byte[] file;
}