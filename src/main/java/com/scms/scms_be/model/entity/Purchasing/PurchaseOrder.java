package com.scms.scms_be.model.entity.Purchasing;

import java.time.LocalDateTime;
import java.util.List;

import com.scms.scms_be.model.entity.General.Company;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    
    private LocalDateTime createdOn;

    private LocalDateTime lastUpdatedOn;

    private String status;

     @OneToMany(mappedBy = "po", orphanRemoval = true , cascade = CascadeType.ALL)
    private List<PurchaseOrderDetail> purchaseOrderDetails;
}

