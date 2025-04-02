package com.scms.scms_be.model.entity.Manufacturing;

import com.scms.scms_be.model.entity.General.Item;

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
@Table(name = "manufacture_stage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufactureStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stageId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    
    private String stageName;
    private Integer orderIndex;
    private Integer estimatedTime; // minutes
    private String description;
}
