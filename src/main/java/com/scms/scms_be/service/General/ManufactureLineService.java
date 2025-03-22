package com.scms.scms_be.service.General;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.General.ManufactureLine;
import com.scms.scms_be.model.entity.General.ManufacturePlant;
import com.scms.scms_be.repository.General.ManufactureLineRepository;
import com.scms.scms_be.repository.General.ManufacturePlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufactureLineService {

    @Autowired
    private ManufactureLineRepository lineRepo;

    @Autowired
    private ManufacturePlantRepository plantRepo;

    public ManufactureLine createLine(Long plantId, ManufactureLine line) {
        ManufacturePlant plant = plantRepo.findById(plantId)
                .orElseThrow(() -> new CustomException("Nhà máy không tồn tại!", HttpStatus.NOT_FOUND));

        if (lineRepo.existsByLineCode(line.getLineCode())) {
            throw new CustomException("Mã dây chuyền đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        line.setPlant(plant);
        return lineRepo.save(line);
    }

    public List<ManufactureLine> getAllLinesInPlant(Long plantId) {
        return lineRepo.findByPlantPlantId(plantId);
    }

    public ManufactureLine getLineById(Long lineId) {
        return lineRepo.findById(lineId)
                .orElseThrow(() -> new CustomException("Dây chuyền không tồn tại!", HttpStatus.NOT_FOUND));
    }

    public ManufactureLine updateLine(Long lineId, ManufactureLine updatedLine) {
        ManufactureLine existingLine = lineRepo.findById(lineId)
                .orElseThrow(() -> new CustomException("Dây chuyền không tồn tại!", HttpStatus.NOT_FOUND));

        if(!existingLine.getLineCode().equals(updatedLine.getLineCode())){
            if (lineRepo.existsByLineCode(updatedLine.getLineCode())) {
                throw new CustomException("Mã dây chuyền đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
        }
        existingLine.setLineName(updatedLine.getLineName());
        existingLine.setCapacity(updatedLine.getCapacity());
        existingLine.setDescription(updatedLine.getDescription());

        return lineRepo.save(existingLine);
    }
}
