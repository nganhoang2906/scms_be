package com.scms.scms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.entity.WarehouseLocation;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {

}
