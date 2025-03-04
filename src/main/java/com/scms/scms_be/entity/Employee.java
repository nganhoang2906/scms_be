package com.scms.scms_be.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "employee")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  employeeId;
    
    private int companyId;
    private String employeeCode;
    private String employeeName;
    private String position;
    private String department;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String gender;
    private String nationalId;
    private String address;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatar;

    private String status;
}
