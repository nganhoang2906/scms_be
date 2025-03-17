package com.scms.scms_be.model.entity.General;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manufacture_plant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturePlant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company; // Mỗi nhà máy thuộc một công ty

    @Column(unique = true, nullable = false)
    private String plantCode; // Mã nhà máy duy nhất

    @Column(nullable = false)
    private String plantName; // Tên nhà máy

    private String description; // Mô tả nhà máy
}
