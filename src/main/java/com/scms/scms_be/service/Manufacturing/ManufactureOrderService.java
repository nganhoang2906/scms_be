package com.scms.scms_be.service.Manufacturing;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.ManufactureOrderDto;
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

    public ManufactureOrderDto createOrder( Long itemId, Long lineId ,ManufactureOrder order) {
        order.setItem(itemRepo.findById(itemId).orElseThrow(() 
            -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND)));
        order.setLine(lineRepo.findById(lineId).orElseThrow(() 
            -> new CustomException("Line không tồn tại", HttpStatus.NOT_FOUND)));
        order.setCreatedOn(new Date());
        order.setLastUpdatedOn(new Date());
        order.setMoCode(generateMOCode(itemId, lineId));
        return convertToDto(moRepo.save(order));
    }

    public List<ManufactureOrderDto> getAllManufactureOrdersbyItemId(Long itemId) {
        return moRepo.findByItem_ItemId(itemId)
                     .stream()
                     .map(this::convertToDto)
                     .collect(Collectors.toList());
    }

    public List<ManufactureOrderDto> getAllManufactureOrdersByCompanyId(Long companyId) {
        return moRepo.findByItem_Company_CompanyId(companyId)
                     .stream()
                     .map(this::convertToDto)
                     .collect(Collectors.toList());
    }

    public ManufactureOrderDto getById(Long moId) {
        ManufactureOrder mo = moRepo.findById(moId)
                                    .orElseThrow(() -> new CustomException("MO không tồn tại", HttpStatus.NOT_FOUND));
        return convertToDto(mo);
    }

    public ManufactureOrderDto update(Long id, ManufactureOrder update) {
        ManufactureOrder mo = moRepo.findById(id)
                                    .orElseThrow(() -> new CustomException("MO không tồn tại", HttpStatus.NOT_FOUND));

        mo.setBatchNo(update.getBatchNo());
        mo.setType(update.getType());
        mo.setQuantity(update.getQuantity());
        mo.setEstimatedStartTime(update.getEstimatedStartTime());
        mo.setEstimatedEndTime(update.getEstimatedEndTime());
        mo.setStatus(update.getStatus());
        mo.setLastUpdatedOn(new Date());

        return convertToDto(moRepo.save(mo));
    }
    public String generateMOCode(Long itemId, Long lineId) {
        String moCode = "MO-"+itemId + "-" + lineId + "-" + System.currentTimeMillis();
        return moCode;
    }

    private ManufactureOrderDto convertToDto(ManufactureOrder mo) {
        ManufactureOrderDto dto = new ManufactureOrderDto();
        dto.setMoId(mo.getMoId());
        dto.setItemId(mo.getItem().getItemId());
        dto.setLineId(mo.getLine().getLineId());
        dto.setMoCode(mo.getMoCode());
        dto.setBatchNo(mo.getBatchNo());
        dto.setType(mo.getType());
        dto.setQuantity(mo.getQuantity());
        dto.setEstimatedStartTime(mo.getEstimatedStartTime());
        dto.setEstimatedEndTime(mo.getEstimatedEndTime());
        dto.setCreatedBy(mo.getCreatedBy());
        dto.setCreatedOn(mo.getCreatedOn());
        dto.setLastUpdatedOn(mo.getLastUpdatedOn());
        dto.setStatus(mo.getStatus());
        return dto;
    }
}
