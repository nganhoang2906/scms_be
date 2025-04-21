package com.scms.scms_be.model.entity.Manufacturing;

import java.util.List;

import com.scms.scms_be.model.entity.General.Item;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column(nullable = false, unique = true)
    private String stageCode; 

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    
    private String description;
    private String status;

    @OneToMany(mappedBy = "stage", orphanRemoval = true , cascade = CascadeType.ALL)
    private List<ManufactureStageDetail> stageDetails; // List of stage details associated with this stage
}
