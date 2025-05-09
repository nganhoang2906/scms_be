package com.scms.scms_be.repository.Manufacturing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Manufacturing.ManufactureStageDetail;

public interface ManufactureStageDetailRepository extends JpaRepository<ManufactureStageDetail, Long> {
  
  List<ManufactureStageDetail> findByStage_StageId(Long stageId);

}
