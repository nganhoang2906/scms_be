package com.scms.scms_be.model.entity.General;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(unique = true, nullable = false)
    private String itemCode;

    private String itemName;
    private String itemType;
    private String uom; // Đơn vị tính (Unit Of Measure)
    private String technicalSpecifications;
    private Double unitPrice;
    private String description;
}
