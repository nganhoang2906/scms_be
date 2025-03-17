package com.scms.scms_be.model.entity.General;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company; // Mỗi kho hàng thuộc một công ty

    @Column(unique = true, nullable = false)
    private String warehouseCode; // Mã kho hàng duy nhất

    @Column(nullable = false)
    private String warehouseName; // Tên kho hàng

    private String description; // Mô tả kho

    @Column(nullable = false)
    private double maxCapacity; // Sức chứa tối đa

    @Column(nullable = false)
    private String warehouseType; // Loại kho hàng (cold storage, general storage, etc.)

    @Column(nullable = false)
    private String status; // Trạng thái (Active, Inactive)
}
