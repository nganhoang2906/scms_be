package com.scms.scms_be.model.entity.Sales;

import java.time.LocalDateTime;

import com.scms.scms_be.model.entity.General.Company;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @ManyToOne
    @JoinColumn(name = "sales_company_id")
    private Company salesCompany;

    @ManyToOne
    @JoinColumn(name = "purchare_company_id")
    private Company purchaseCompany;

    @OneToOne
    @JoinColumn(name = "so_id")
    private SalesOrder salesOrder;

    @Column(unique = true, nullable = false)
    private String invoiceCode;

    private Double subTotal;
    private Double taxRate;
    private Double taxAmount;
    private Double totalAmount;
    private String paymentMethod;
    private String createBy;
    private LocalDateTime createdOn;
    private String seller;
    private String buyer;
    private String status;
    private String file;
}