package com.scms.scms_be.repository.Purchasing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scms.scms_be.model.entity.Purchasing.RfqDetail;

public interface RfqDetailRepository extends JpaRepository<RfqDetail, Long> {

    List<RfqDetail> findByRfq_RfqId(Long rfqId);

}
