package com.scms.scms_be.model.entity.General;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(unique = true, nullable = false)
    private String employeeCode;

    private String employeeName;
    private String position;
    private String gender;
    
    @Column(unique = true, nullable = false)
    private String identityNumber;
    private String address;
    private String email;
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Temporal(TemporalType.DATE)
    private Date employmentStartDate;

    @Lob
    private byte[] avatar;

    private String status;
}
