package com.scms.scms_be.model.entity.General;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Long currentCompanyId;

    private String serialNumber;
    private Long batchId;
    private String qrCode;
}

