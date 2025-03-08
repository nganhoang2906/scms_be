package com.scms.scms_be.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scms.scms_be.model.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Optional<Company> findByTaxCode(String taxCode);

}
