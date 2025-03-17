package com.scms.scms_be.repository.General;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.General.ManufactureLine;

@Repository
public interface ManufactureLineRepository extends JpaRepository<ManufactureLine, Long> {

}
