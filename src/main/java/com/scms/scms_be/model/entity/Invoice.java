package com.scms.scms_be.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String address;
    private String company;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdfData;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String productList;

    private LocalDateTime createAt;
}
