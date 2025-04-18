package com.scms.scms_be.service.Manufacturing;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.dto.Manufacture.ManufactureProcessDto;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureProcess;
import com.scms.scms_be.model.request.Manufacturing.ManuProcessRequest;
import com.scms.scms_be.repository.Manufacturing.ManufactureOrderRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureProcessRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageRepository;

@Service
public class ManufactureProcessService {

    @Autowired 
    private ManufactureProcessRepository processRepo;
    @Autowired 
    private ManufactureOrderRepository orderRepo;
    @Autowired 
    private ManufactureStageRepository stageRepo;

    public ManufactureProcessDto create( Long moId, Long stageId,ManuProcessRequest processRequest) {
        ManufactureProcess process = new ManufactureProcess();
        process.setStartedOn(processRequest.getStartedOn());
        process.setFinishedOn(processRequest.getFinishedOn());
        process.setStatus(processRequest.getStatus());
        
        process.setOrder(orderRepo.findById(moId)
            .orElseThrow(() -> new CustomException("MO không tồn tại", HttpStatus.NOT_FOUND)));
        process.setStage(stageRepo.findById(stageId)
            .orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND)));
        
            return convertToDto(processRepo.save(process));
    }

    public List<ManufactureProcessDto> getAllByCompany(Long companyId) {
        return processRepo.findByOrder_Item_Company_CompanyId(companyId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ManufactureProcessDto getById(Long id) {
        ManufactureProcess process = processRepo.findById(id)
            .orElseThrow(() -> new CustomException("Process không tồn tại", HttpStatus.NOT_FOUND));
        return convertToDto(process);
    }

    public ManufactureProcessDto update(Long id, ManuProcessRequest processUpdate) {
        ManufactureProcess process = processRepo.findById(id)
            .orElseThrow(() -> new CustomException("Process không tồn tại", HttpStatus.NOT_FOUND));
        process.setStartedOn(processUpdate.getStartedOn());
        process.setFinishedOn(processUpdate.getFinishedOn());
        process.setStatus(processUpdate.getStatus());
        return convertToDto(processRepo.save(process));
    }

    private ManufactureProcessDto convertToDto(ManufactureProcess process) {
        ManufactureProcessDto dto = new ManufactureProcessDto();
        dto.setId(process.getId());

        dto.setMoId(process.getOrder().getMoId());
        dto.setMoCode(process.getOrder().getMoCode());

        dto.setStageId(process.getStage().getStageId());
        dto.setStageName(process.getStage().getStageName());
        dto.setStageOrder(process.getStage().getStageOrder());


        dto.setStartedOn(process.getStartedOn());

        dto.setFinishedOn(process.getFinishedOn());
        dto.setStatus(process.getStatus());
        return dto;
    }
}
