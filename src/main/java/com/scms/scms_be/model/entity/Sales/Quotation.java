package com.scms.scms_be.model.entity.Sales;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.scms.scms_be.model.entity.General.Company;
import com.scms.scms_be.model.entity.Purchasing.RequestForQuotation;

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
@Table(name = "quotation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quotationId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "rfq_id")
    private RequestForQuotation rfq;

    @Column(unique = true, nullable = false)
    private String quotationCode;

    private LocalDate availableByDate;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
    private String status;
}