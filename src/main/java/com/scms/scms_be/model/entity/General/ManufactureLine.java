package com.scms.scms_be.model.entity.General;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manufacture_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineId;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private ManufacturePlant plant; // Mỗi dây chuyền thuộc một nhà máy

    @Column(unique = true, nullable = false)
    private String lineCode; // Mã dây chuyền duy nhất

    @Column(nullable = false)
    private String lineName; // Tên dây chuyền sản xuất

    @Column(nullable = false)
    private double capacity; // Công suất dây chuyền

    private String description; // Mô tả dây chuyền
}
