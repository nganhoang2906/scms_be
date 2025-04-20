package com.scms.scms_be.model.entity.Purchasing;

import java.time.LocalDateTime;

import com.scms.scms_be.model.entity.General.Company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "request_for_quotation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestForQuotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rfqId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, unique = true)
    private String rfqCode;

    @ManyToOne
    @JoinColumn(name = "requested_company_id", nullable=false)
    private Company requestedCompany;

     
    private LocalDateTime needByDate;

    private String createdBy;

    private LocalDateTime createdOn;

    private LocalDateTime lastUpdatedOn;

    private String status;
}
