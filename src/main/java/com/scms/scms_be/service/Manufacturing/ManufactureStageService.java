package com.scms.scms_be.service.Manufacturing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.ManufactureStageDetailDto;
import com.scms.scms_be.model.dto.Manufacture.ManufactureStageDto;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStage;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureStageDetail;
import com.scms.scms_be.model.request.Manufacturing.ManuStageDetailRequest;
import com.scms.scms_be.model.request.Manufacturing.ManuStageRequest;
import com.scms.scms_be.repository.General.ItemRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageDetailRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageRepository;

@Service
public class ManufactureStageService {

  @Autowired
  private ManufactureStageRepository stageRepo;

  @Autowired
  private ItemRepository itemRepo;

  @Autowired
  private ManufactureStageDetailRepository stageDetailRepo;

  public ManufactureStageDto createStage(ManuStageRequest stageRequest) {
    ManufactureStage stage = new ManufactureStage();
    stage.setStageCode(generateStageCode(stageRequest.getItemId()));

    stage.setDescription(stageRequest.getDescription());
    stage.setStatus(stageRequest.getStatus());

    stage.setItem(itemRepo.findById(stageRequest.getItemId())
        .orElseThrow(() -> new CustomException("Không tìm thấy hàng hóa!", HttpStatus.NOT_FOUND)));

    ManufactureStage saveStage = stageRepo.save(stage);

    if (stageRequest.getStageDetails() == null || stageRequest.getStageDetails().isEmpty()) {
      throw new CustomException("Danh sách công đoạn không được để trống!", HttpStatus.BAD_REQUEST);
    }

    for (ManuStageDetailRequest detailRequest : stageRequest.getStageDetails()) {
      ManufactureStageDetail stageDetail = new ManufactureStageDetail();

      stageDetail.setStage(saveStage);
      stageDetail.setStageName(detailRequest.getStageName());
      stageDetail.setStageOrder(detailRequest.getStageOrder());
      stageDetail.setEstimatedTime(detailRequest.getEstimatedTime());
      stageDetail.setDescription(detailRequest.getDescription());

      stageDetailRepo.save(stageDetail);
    }

    return convertToDto(saveStage);
  }

  public String generateStageCode(Long itemId) {
    String prefix = "MS" + itemId;
    int count = stageRepo.countByStageCodeStartingWith(prefix);
    return prefix + String.format("%02d", count + 1);
  }

  public ManufactureStageDto getStagesByItemId(Long itemId) {
    return convertToDto(stageRepo.findByItem_ItemId(itemId));
  }

  public ManufactureStageDto getStageById(Long stageId) {
    ManufactureStage stage = stageRepo.findById(stageId)
        .orElseThrow(() -> new CustomException("Không tìm thấy công đoạn sản xuất!", HttpStatus.NOT_FOUND));
    return convertToDto(stage);
  }

  public List<ManufactureStageDto> getAllStagesInCompany(Long companyId) {
    List<ManufactureStage> stages = stageRepo.findByItem_Company_CompanyId(companyId);
    return stages.stream()
        .map(this::convertToDto)
        .toList();
  }

  public ManufactureStageDto updateStage(Long stageId, ManuStageRequest stage) {
    ManufactureStage exist = stageRepo.findById(stageId)
        .orElseThrow(() -> new CustomException("Không tìm thấy công đoạn sản xuất!", HttpStatus.NOT_FOUND));

    exist.setDescription(stage.getDescription());
    exist.setStatus(stage.getStatus());

    if (stage.getStageDetails() == null || stage.getStageDetails().isEmpty()) {
      throw new CustomException("Danh sách công đoạn không được để trống!", HttpStatus.BAD_REQUEST);
    }

    for (ManuStageDetailRequest detailRequest : stage.getStageDetails()) {
      ManufactureStageDetail stageDetail = new ManufactureStageDetail();
      stageDetail.setStage(exist);
      stageDetail.setStageName(detailRequest.getStageName());
      stageDetail.setStageOrder(detailRequest.getStageOrder());
      stageDetail.setEstimatedTime(detailRequest.getEstimatedTime());
      stageDetail.setDescription(detailRequest.getDescription());

      stageDetailRepo.save(stageDetail);
    }
    return convertToDto(stageRepo.save(exist));
  }

  public void deleteStage(Long stageId) {
    ManufactureStage exist = stageRepo.findById(stageId)
        .orElseThrow(() -> new CustomException("Không tìm thấy công đoạn sản xuất!", HttpStatus.NOT_FOUND));
    stageRepo.delete(exist);
  }

  private ManufactureStageDto convertToDto(ManufactureStage stage) {
    ManufactureStageDto dto = new ManufactureStageDto();
    dto.setStageId(stage.getStageId());
    dto.setStageCode(stage.getStageCode());
    dto.setItemId(stage.getItem().getItemId());
    dto.setItemCode(stage.getItem().getItemCode());
    dto.setItemName(stage.getItem().getItemName());
    dto.setDescription(stage.getDescription());
    dto.setStatus(stage.getStatus());

    List<ManufactureStageDetailDto> details = stageDetailRepo
        .findByStage_StageId(stage.getStageId())
        .stream()
        .map(this::convertToDetailDto)
        .toList();
    dto.setStageDetails(details);

    return dto;
  }

  public ManufactureStageDetailDto convertToDetailDto(ManufactureStageDetail stageDetail) {
    ManufactureStageDetailDto dto = new ManufactureStageDetailDto();
    dto.setStageDetailId(stageDetail.getStageDetailId());
    dto.setStageId(stageDetail.getStage().getStageId());
    dto.setStageName(stageDetail.getStageName());
    dto.setStageOrder(stageDetail.getStageOrder());
    dto.setEstimatedTime(stageDetail.getEstimatedTime());
    dto.setDescription(stageDetail.getDescription());
    return dto;
  }
}
