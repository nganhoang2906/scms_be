package com.scms.scms_be.service.Manufacturing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.ManufactureStageDto;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageRepository;

@Service
public class ManufactureStageService {

    @Autowired
    private ManufactureStageRepository stageRepo;

    @Autowired
    private ItemRepository itemRepo;

    // Mapper thủ công
    private ManufactureStageDto toDto(ManufactureStage stage) {
        ManufactureStageDto dto = new ManufactureStageDto();
        dto.setStageId(stage.getStageId());
        dto.setItemId(stage.getItem().getItemId());
        dto.setStageName(stage.getStageName());
        dto.setStageOrder(stage.getStageOrder());
        dto.setEstimatedTime(stage.getEstimatedTime());
        dto.setDescription(stage.getDescription());
        return dto;
    }

    public ManufactureStageDto createStage(Long itemId, ManufactureStage stage) {
        stage.setItem(itemRepo.findById(itemId)
                .orElseThrow(() -> new CustomException("Item không tồn tại", HttpStatus.NOT_FOUND)));
        ManufactureStage saved = stageRepo.save(stage);
        return toDto(saved);
    }

    public List<ManufactureStageDto> getAllStagesByItemId(Long itemId) {
        return stageRepo.findByItem_ItemId(itemId).stream().map(this::toDto).toList();
    }

    public ManufactureStageDto getStageById(Long id) {
        ManufactureStage stage = stageRepo.findById(id)
                .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND));
        return toDto(stage);
    }

    public ManufactureStageDto updateStage(Long id, ManufactureStage stage) {
        ManufactureStage exist = stageRepo.findById(id)
                .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND));
        exist.setStageName(stage.getStageName());
        exist.setStageOrder(stage.getStageOrder());
        exist.setEstimatedTime(stage.getEstimatedTime());
        exist.setDescription(stage.getDescription());
        ManufactureStage updated = stageRepo.save(exist);
        return toDto(updated);
    }

    public void deleteStage(Long id) {
        ManufactureStage exist = stageRepo.findById(id)
                .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND));
        stageRepo.delete(exist);
    }
}
