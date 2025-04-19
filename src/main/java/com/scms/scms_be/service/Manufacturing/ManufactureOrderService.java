package com.scms.scms_be.service.Manufacturing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.ManufactureOrderDto;
import com.scms.scms_be.model.dto.Manufacture.ManufactureProcessDto;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureOrder;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStageDetail;
import com.scms.scms_be.model.request.Manufacturing.ManuOrderRequest;
import com.scms.scms_be.model.request.Manufacturing.ManuProcessRequest;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.General.ManufactureLineRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureOrderRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageDetailRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageRepository;

@Service
public class ManufactureOrderService {

    @Autowired 
    private ManufactureOrderRepository moRepo;

    @Autowired 
    private ItemRepository itemRepo;

    @Autowired 
    private ManufactureLineRepository lineRepo;

    @Autowired
    private ManufactureStageRepository stageRepo;

    @Autowired
    private ManufactureStageDetailRepository stageDetailRepo;

    @Autowired
    private ManufactureProcessService processService;

    public ManufactureOrderDto createOrder(ManuOrderRequest orderRequest) {
        

        ManufactureOrder order = new ManufactureOrder();
        order.setMoCode(generateMOCode(orderRequest.getItemId(), orderRequest.getLineId()));    
        order.setType(orderRequest.getType());
        order.setQuantity(orderRequest.getQuantity());
        order.setEstimatedStartTime(orderRequest.getEstimatedStartTime());
        order.setEstimatedEndTime(orderRequest.getEstimatedEndTime());
        order.setCreatedBy(orderRequest.getCreatedBy());
        
        order.setItem(itemRepo.findById(orderRequest.getItemId()).orElseThrow(() 
            -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND)));
        order.setLine(lineRepo.findById(orderRequest.getLineId()).orElseThrow(() 
            -> new CustomException("Line không tồn tại", HttpStatus.NOT_FOUND)));
        order.setCreatedOn( LocalDateTime.now());
        order.setLastUpdatedOn( LocalDateTime.now());
        order.setStatus(orderRequest.getStatus());

        moRepo.save(order);
        ManufactureStage stage = stageRepo.findByItem_ItemId(orderRequest.getItemId());
        List<ManufactureStageDetail> stageDetailList= stageDetailRepo.findByStage_StageId(stage.getStageId());
        for(ManufactureStageDetail  stageDetail : stageDetailList) {
            ManuProcessRequest processRequest = new ManuProcessRequest();
            processRequest.setMoId(order.getMoId());
            processRequest.setStageDetailId(stageDetail.getStageDetailId());
            
            processService.createManuProcess(processRequest);
        }

        throw new CustomException("Tạo MO thành công", HttpStatus.OK);
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

    public ManufactureOrderDto update(Long id, ManuOrderRequest update) {
        ManufactureOrder mo = moRepo.findById(id)
                                    .orElseThrow(() -> new CustomException("MO không tồn tại", HttpStatus.NOT_FOUND));

        mo.setType(update.getType());
        mo.setQuantity(update.getQuantity());
        mo.setEstimatedStartTime(update.getEstimatedStartTime());
        mo.setEstimatedEndTime(update.getEstimatedEndTime());
        mo.setCreatedBy(update.getCreatedBy());
        mo.setLastUpdatedOn( LocalDateTime.now());
        mo.setStatus(update.getStatus());
        
        return convertToDto(moRepo.save(mo));
    }
    public String generateMOCode(Long itemId, Long lineId) {
        int count = moRepo.countByItemItemIdAndLineLineId(itemId, lineId);
        return "MO" + itemId  + lineId +  String.format("%d", count + 1);
    }
    

    private ManufactureOrderDto convertToDto(ManufactureOrder mo) {
        ManufactureOrderDto dto = new ManufactureOrderDto();
        dto.setMoId(mo.getMoId());
        dto.setMoCode(mo.getMoCode());

        dto.setItemId(mo.getItem().getItemId());
        dto.setItemCode(mo.getItem().getItemCode());
        dto.setItemName(mo.getItem().getItemName());

        dto.setLineId(mo.getLine().getLineId());
        dto.setLineCode(mo.getLine().getLineCode());
        dto.setLineName(mo.getLine().getLineName());

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
