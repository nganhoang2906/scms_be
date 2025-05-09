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
import com.scms.scms_be.repository.Manufacturing.ManufactureStageDetailRepository;

@Service
public class ManufactureProcessService {

  @Autowired
  private ManufactureProcessRepository processRepo;
  @Autowired
  private ManufactureOrderRepository orderRepo;
  @Autowired
  private ManufactureStageDetailRepository stageDetailRepo;

  public ManufactureProcessDto createManuProcess(ManuProcessRequest processRequest) {
    ManufactureProcess process = new ManufactureProcess();
    process.setStartedOn(processRequest.getStartedOn());
    process.setFinishedOn(processRequest.getFinishedOn());
    process.setStatus("Chưa thực hiện");

    process.setOrder(orderRepo.findById(processRequest.getMoId())
        .orElseThrow(() -> new CustomException("Không tìm thấy công lệnh sản xuất!", HttpStatus.NOT_FOUND)));
    process.setStageDetail(stageDetailRepo.findById(processRequest.getStageDetailId())
        .orElseThrow(() -> new CustomException("Chưa thiết lập công đoạn sản xuất cho hàng hóa này!", HttpStatus.NOT_FOUND)));

    return convertToDto(processRepo.save(process));
  }

  public List<ManufactureProcessDto> getAllByMoId(Long moId) {
    List<ManufactureProcess> processes = processRepo.findByOrder_MoId(moId);
    return processes.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  public ManufactureProcessDto getById(Long id) {
    ManufactureProcess process = processRepo.findById(id)
        .orElseThrow(() -> new CustomException("Không tìm thấy quá trình sản xuất!", HttpStatus.NOT_FOUND));
    return convertToDto(process);
  }

  public ManufactureProcessDto update(Long id, ManuProcessRequest processUpdate) {
    ManufactureProcess process = processRepo.findById(id)
        .orElseThrow(() -> new CustomException("Không tìm thấy quá trình sản xuất!", HttpStatus.NOT_FOUND));
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

    dto.setStageDetailId(process.getStageDetail().getStageDetailId());
    dto.setStageDetailName(process.getStageDetail().getStageName());
    dto.setStageDetailOrder(process.getStageDetail().getStageOrder());

    dto.setStartedOn(process.getStartedOn());

    dto.setFinishedOn(process.getFinishedOn());
    dto.setStatus(process.getStatus());
    return dto;
  }
}
