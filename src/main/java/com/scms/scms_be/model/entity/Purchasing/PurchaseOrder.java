package com.scms.scms_be.model.entity.Purchasing;

import java.util.Date;

import com.scms.scms_be.model.entity.General.Company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "purchase_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable=false)
    private Company company;

    @Column(nullable = false, unique = true)
    private String poCode;

    @ManyToOne
    @JoinColumn(name = "suplier_company_id", nullable=false)
    private Company suplierCompany;

    private String description;
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    private String status;
}

