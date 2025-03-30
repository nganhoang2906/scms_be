package com.scms.scms_be.repository.Manufacturing;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Manufaacturing.BOMDetail;

public interface BOMDetailRepository extends JpaRepository<BOMDetail, Long> {

}
