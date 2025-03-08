package com.scms.scms_be.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long companyId;

    private String companyCode;
    private String taxCode;
    private String companyName;
    private String headOfficeAddress;
    private String mainIndustry;
    private String representativeName;

    private Date startDate;

    private Date joinDate;
    
    private String companyType;
    private String phoneNumber;
    private String email;
    private String website;
    
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] logo;

    private String status;
    private String  country;

}
