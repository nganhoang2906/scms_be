package com.scms.scms_be.repository.General;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.General.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCompanyCompanyId(Long companyId);
    boolean existsByItemCode(String itemCode);
}