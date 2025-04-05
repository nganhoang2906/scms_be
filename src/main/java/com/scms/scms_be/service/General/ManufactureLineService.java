package com.scms.scms_be.service.General;

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
import com.scms.scms_be.repository.General.ManufactureLineRepository;
import com.scms.scms_be.repository.General.ManufacturePlantRepository;

@Service
public class ManufactureLineService {

    @Autowired
    private ManufactureLineRepository lineRepo;

    @Autowired
    private ManufacturePlantRepository plantRepo;

    public ManufactureLineDto createLine(Long plantId, ManufactureLine line) {
        ManufacturePlant plant = plantRepo.findById(plantId)
                .orElseThrow(() -> new CustomException("Nhà máy không tồn tại!", HttpStatus.NOT_FOUND));

        if (lineRepo.existsByLineCode(line.getLineCode())) {
            throw new CustomException("Mã dây chuyền đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        line.setPlant(plant);
        return convertToDto(lineRepo.save(line));
    }

    public List<ManufactureLineDto> getAllLinesInPlant(Long plantId) {
        List<ManufactureLine> lines = lineRepo.findByPlantPlantId(plantId);
        return lines.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ManufactureLineDto getLineById(Long lineId) {
        ManufactureLine line = lineRepo.findById(lineId)
                .orElseThrow(() -> new CustomException("Dây chuyền không tồn tại!", HttpStatus.NOT_FOUND));
        return convertToDto(line);
    }

    public ManufactureLineDto updateLine(Long lineId, ManufactureLine updatedLine) {
        ManufactureLine existingLine = lineRepo.findById(lineId)
                .orElseThrow(() -> new CustomException("Dây chuyền không tồn tại!", HttpStatus.NOT_FOUND));

        if (!existingLine.getLineCode().equals(updatedLine.getLineCode())) {
            if (lineRepo.existsByLineCode(updatedLine.getLineCode())) {
                throw new CustomException("Mã dây chuyền đã tồn tại!", HttpStatus.BAD_REQUEST);
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
        dto.setPlantId(line.getPlant().getPlantId());
        dto.setLineId(line.getLineId());
        dto.setLineCode(line.getLineCode());
        dto.setLineName(line.getLineName());
        dto.setCapacity(line.getCapacity());
        dto.setDescription(line.getDescription());

        return dto;
    }
}
