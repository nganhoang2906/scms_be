package com.scms.scms_be.service.General;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.General.ManufactureLineDto;
import com.scms.scms_be.model.entity.General.ManufactureLine;
import com.scms.scms_be.model.entity.General.ManufacturePlant;
import com.scms.scms_be.model.request.General.ManuLineRequest;
import com.scms.scms_be.repository.General.ManufactureLineRepository;
import com.scms.scms_be.repository.General.ManufacturePlantRepository;

@Service
public class ManufactureLineService {

  @Autowired
  private ManufactureLineRepository lineRepo;

  @Autowired
  private ManufacturePlantRepository plantRepo;

  public ManufactureLineDto createLine(Long plantId, ManuLineRequest newLine) {
    ManufacturePlant plant = plantRepo.findById(plantId)
        .orElseThrow(() -> new CustomException("Không tìm thấy xưởng sản xuất!", HttpStatus.NOT_FOUND));

    if (lineRepo.existsByLineCode(newLine.getLineCode())) {
      throw new CustomException("Mã dây chuyền đã được sử dụng!", HttpStatus.BAD_REQUEST);
    }
    ManufactureLine line = new ManufactureLine();
    line.setPlant(plant);
    line.setLineCode(newLine.getLineCode());
    line.setLineName(newLine.getLineName());
    line.setCapacity(newLine.getCapacity());
    line.setDescription(newLine.getDescription());
    return convertToDto(lineRepo.save(line));
  }

  public String generateLineCode(Long companyId) {
    String prefix = "ML" + String.format("%04d", companyId);
    int count = lineRepo.countByLineCodeStartingWith(prefix);
    return prefix + String.format("%03d", count + 1);
  }

  public List<ManufactureLineDto> getAllLinesInPlant(Long plantId) {
    List<ManufactureLine> lines = lineRepo.findByPlantPlantId(plantId);
    return lines.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  public List<ManufactureLineDto> getAllLinesInCompany(Long companyId) {
    List<ManufacturePlant> plants = plantRepo.findByCompanyCompanyId(companyId);
    List<ManufactureLineDto> allLines = new ArrayList<>();

    for (ManufacturePlant plant : plants) {
      List<ManufactureLine> lines = lineRepo.findByPlantPlantId(plant.getPlantId());
      allLines.addAll(lines.stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    return allLines;
  }

  public ManufactureLineDto getLineById(Long lineId) {
    ManufactureLine line = lineRepo.findById(lineId)
        .orElseThrow(() -> new CustomException("Không tìm thấy dây chuyền sản xuất!", HttpStatus.NOT_FOUND));
    return convertToDto(line);
  }

  public ManufactureLineDto updateLine(Long lineId, ManuLineRequest updatedLine) {
    ManufactureLine existingLine = lineRepo.findById(lineId)
        .orElseThrow(() -> new CustomException("Không tìm thấy dây chuyền sản xuất!", HttpStatus.NOT_FOUND));

    if (!existingLine.getLineCode().equals(updatedLine.getLineCode())) {
      if (lineRepo.existsByLineCode(updatedLine.getLineCode())) {
        throw new CustomException("Mã dây chuyền đã được sử dụng!", HttpStatus.BAD_REQUEST);
      }
    }

    existingLine.setLineName(updatedLine.getLineName());
    existingLine.setLineCode(updatedLine.getLineCode());
    existingLine.setCapacity(updatedLine.getCapacity());
    existingLine.setDescription(updatedLine.getDescription());

    return convertToDto(lineRepo.save(existingLine));
  }

  public boolean deleteLine(Long lineId) {
    Optional<ManufactureLine> line = lineRepo.findById(lineId);
    if (line.isPresent()) {
      lineRepo.delete(line.get());
      return true;
    }
    return false;
  }

  private ManufactureLineDto convertToDto(ManufactureLine line) {
    ManufactureLineDto dto = new ManufactureLineDto();
    dto.setCompanyId(line.getPlant().getCompany().getCompanyId());
    dto.setPlantId(line.getPlant().getPlantId());
    dto.setPlantName(line.getPlant().getPlantName());
    dto.setLineId(line.getLineId());
    dto.setLineCode(line.getLineCode());
    dto.setLineName(line.getLineName());
    dto.setCapacity(line.getCapacity());
    dto.setDescription(line.getDescription());

    return dto;
  }
}
