package com.scms.scms_be.model.entity.Manufacturing;

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
@Table(name = "bom_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOMDetail {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bom_id", nullable = false)
    private BOM bom;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Double quantity;
    private String note;
    
}
