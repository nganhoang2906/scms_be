package com.scms.scms_be.service.Manufacturing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scms.scms_be.exception.CustomException;
import com.scms.scms_be.model.entity.Manufacturing.ManufactureProcess;
import com.scms.scms_be.repository.Manufacturing.ManufactureOrderRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureProcessRepository;
import com.scms.scms_be.repository.Manufacturing.ManufactureStageRepository;

@Service
public class ManufactureProcessService {

    @Autowired private ManufactureProcessRepository processRepo;
    @Autowired private ManufactureOrderRepository orderRepo;
    @Autowired private ManufactureStageRepository stageRepo;

    public ManufactureProcess create(ManufactureProcess process, Long moId, Long stageId) {
        process.setOrder(orderRepo.findById(moId).orElseThrow(() -> new CustomException("MO không tồn tại", HttpStatus.NOT_FOUND)));
        process.setStage(stageRepo.findById(stageId).orElseThrow(() -> new CustomException("Stage không tồn tại", HttpStatus.NOT_FOUND)));
        return processRepo.save(process);
    }

    public List<ManufactureProcess> getAllByCompany(Long companyId) {
        return processRepo.findByOrder_Item_Company_CompanyId(companyId);
    }

    public ManufactureProcess getById(Long id) {
        return processRepo.findById(id).orElseThrow(() -> new CustomException("Process không tồn tại", HttpStatus.NOT_FOUND));
    }

    public ManufactureProcess update(Long id, ManufactureProcess processUpdate) {
        ManufactureProcess process = getById(id);
        process.setStartedOn(processUpdate.getStartedOn());
        process.setFinishedOn(processUpdate.getFinishedOn());
        process.setStatus(processUpdate.getStatus());
        return processRepo.save(process);
    }
}

