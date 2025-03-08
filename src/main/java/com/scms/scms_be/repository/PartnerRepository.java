package com.scms.scms_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

}
