package com.scms.scms_be.repository.Manufacturing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Manufacturing.ManufactureProcess;

@Repository
public interface ManufactureProcessRepository extends JpaRepository<ManufactureProcess, Long> { 
  
}
