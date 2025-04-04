package com.scms.scms_be.model.entity.Manufacturing;

import java.util.Date;

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
@Table(name = "manufacture_process")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn(name = "mo_id") 
    private ManufactureOrder order;
    
    @ManyToOne 
    @JoinColumn(name = "stage_id") 
    private ManufactureStage stage;

    private Date startedOn;
    private Date finishedOn;
    private String status;
}
