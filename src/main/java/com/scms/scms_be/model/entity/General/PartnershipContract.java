package com.scms.scms_be.model.entity.General;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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