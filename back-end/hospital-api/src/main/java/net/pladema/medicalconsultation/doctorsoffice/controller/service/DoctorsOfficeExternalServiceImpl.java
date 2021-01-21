package net.pladema.medicalconsultation.doctorsoffice.controller.service;

import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import net.pladema.medicalconsultation.doctorsoffice.controller.mapper.DoctorsOfficeMapper;
import net.pladema.medicalconsultation.doctorsoffice.service.DoctorsOfficeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DoctorsOfficeExternalServiceImpl implements DoctorsOfficeExternalService{

    private final Logger LOG = LoggerFactory.getLogger(DoctorsOfficeExternalServiceImpl.class);

    private final DoctorsOfficeService doctorsOfficeService;

    private final DoctorsOfficeMapper doctorsOfficeMapper;

    public DoctorsOfficeExternalServiceImpl(DoctorsOfficeService doctorsOfficeService,
                                            DoctorsOfficeMapper doctorsOfficeMapper) {
        this.doctorsOfficeService = doctorsOfficeService;
        this.doctorsOfficeMapper = doctorsOfficeMapper;
    }

    @Override
    public DoctorsOfficeDto getDoctorsOfficeById(Integer doctorsOfficeId) {
        LOG.debug("Input parameter -> doctorsOfficeId {}", doctorsOfficeId);
        DoctorsOfficeDto result = doctorsOfficeMapper.toDoctorsOfficeDto(doctorsOfficeService.getById(doctorsOfficeId));
        LOG.debug("Output -> {}", result);
        return result;
    }
}
