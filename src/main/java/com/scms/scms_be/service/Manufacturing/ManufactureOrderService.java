package com.scms.scms_be.service.Manufacturing;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.ManufactureLineRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureOrderRepository;

@Service
public class ManufactureOrderService {

    @Autowired 
    private ManufactureOrderRepository moRepo;
    @Autowired 
    private ItemRepository itemRepo;
    @Autowired 
    private ManufactureLineRepository lineRepo;

    public ManufactureOrder createOrder(ManufactureOrder order, Long itemId, Long lineId) {
        order.setItem(itemRepo.findById(itemId).orElseThrow(() 
             -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND)));
        order.setLine(lineRepo.findById(lineId).orElseThrow(() 
            -> new CustomException("Line không tồn tại", HttpStatus.NOT_FOUND)));
        order.setCreatedOn(new Date());
        order.setLastUpdatedOn(new Date());
        return moRepo.save(order);
    }

    public List<ManufactureOrder> getAllManufactureOrdersbyItemId(Long itemId) {
        return moRepo.findByItem_ItemId(itemId);
    }
    public List<ManufactureOrder> getAllManufactureOrdersByCompanyId(Long companyId) {
        return moRepo.findByItem_Company_CompanyId(companyId);
    }

    public ManufactureOrder getById(Long moId) {
        return moRepo.findById(moId).orElseThrow(() -> new CustomException("MO không tồn tại", HttpStatus.NOT_FOUND));
    }

    public ManufactureOrder update(Long id, ManufactureOrder update) {
        ManufactureOrder mo = getById(id);
        mo.setBatchNo(update.getBatchNo());
        mo.setType(update.getType());
        mo.setQuantity(update.getQuantity());
        mo.setEstimatedStartTime(update.getEstimatedStartTime());
        mo.setEstimatedEndTime(update.getEstimatedEndTime());
        mo.setStatus(update.getStatus());
        mo.setLastUpdatedOn(new Date());
        return moRepo.save(mo);
    }
}
