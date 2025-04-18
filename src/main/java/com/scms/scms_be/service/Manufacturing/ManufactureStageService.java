package com.scms.scms_be.service.Manufacturing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.ManufactureStageDto;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;
import com.scms.scms_be.model.request.Manufacturing.ManuStageRequest;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageRepository;

@Service
public class ManufactureStageService {

    @Autowired
    private ManufactureStageRepository stageRepo;

    @Autowired
    private ItemRepository itemRepo;


    

    public ManufactureStageDto createStage(Long itemId, ManuStageRequest stageRequest) {
        ManufactureStage stage = new ManufactureStage();
        stage.setStageName(stageRequest.getStageName());
        stage.setStageOrder(stageRequest.getStageOrder());
        stage.setEstimatedTime(stageRequest.getEstimatedTime());
        stage.setDescription(stageRequest.getDescription());
        
        stage.setItem(itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND)));
        ManufactureStage saved = stageRepo.save(stage);
        return convertToDto(saved);
    }

    public List<ManufactureStageDto> getAllStagesByItemId(Long itemId) {
        return stageRepo.findByItem_ItemId(itemId).stream().map(this::convertToDto).toList();
    }

    public ManufactureStageDto getStageById(Long stageId) {
        ManufactureStage stage = stageRepo.findById(stageId)
                .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND));
        return convertToDto(stage);
    }

    public ManufactureStageDto updateStage(Long stageId, ManuStageRequest stage) {
        ManufactureStage exist = stageRepo.findById(stageId)
                .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND));
        
        exist.setStageName(stage.getStageName());
        exist.setStageOrder(stage.getStageOrder());
        exist.setEstimatedTime(stage.getEstimatedTime());
        exist.setDescription(stage.getDescription());
        ManufactureStage updated = stageRepo.save(exist);
        return convertToDto(updated);
    }

    public void deleteStage(Long stageId) {
        ManufactureStage exist = stageRepo.findById(stageId)
                .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND));
        stageRepo.delete(exist);
    }
    
    private ManufactureStageDto convertToDto(ManufactureStage stage) {
        ManufactureStageDto dto = new ManufactureStageDto();
        dto.setStageId(stage.getStageId());

        dto.setItemId(stage.getItem().getItemId());
        dto.setItemCode(stage.getItem().getItemCode());
        dto.setItemName(stage.getItem().getItemName());

        dto.setStageName(stage.getStageName());
        dto.setStageOrder(stage.getStageOrder());
        
        dto.setEstimatedTime(stage.getEstimatedTime());
        dto.setDescription(stage.getDescription());
        return dto;
    }
}
