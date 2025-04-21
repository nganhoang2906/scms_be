package com.scms.scms_be.model.entity.Sales;

import com.scms.scms_be.model.entity.General.Item;

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
@Table(name = "quotation_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quotaionDetailId;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Double quantity;
    private Double itemPrice;
    private String note;
}