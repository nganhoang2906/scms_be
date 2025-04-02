package com.scms.scms_be.repository.Manufacturing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Manufaacturing.BOMDetail;

@Repository
public interface BOMDetailRepository extends JpaRepository<BOMDetail, Long> {

    void deleteByBom_BomId(Long bomId);

    List<BOMDetail> findByBom_BomId(Long bomId);

}
