package net.pladema.medicalconsultation.doctorsoffice.service.impl;

import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.medicalconsultation.doctorsoffice.service.DoctorsOfficeService;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorsOfficeServiceImpl implements DoctorsOfficeService {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorsOfficeServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final DoctorsOfficeRepository doctorsOfficeRepository;

    public DoctorsOfficeServiceImpl(DoctorsOfficeRepository doctorsOfficeRepository){
        super();
        this.doctorsOfficeRepository = doctorsOfficeRepository;
    }

    @Override
    public List<DoctorsOfficeBo> getAllDoctorsOffice(Integer institutionId, Integer sectorId,
                                                               Integer clinicalspecialtyId) {
        LOG.debug("Input parameters -> institutionId {}, sectorId {}, clinicalspecialtyId {}",
                institutionId, sectorId, clinicalspecialtyId);
        List<DoctorsOfficeVo> resultQuery = doctorsOfficeRepository.findAllBy(institutionId, sectorId, clinicalspecialtyId);
        List<DoctorsOfficeBo> result = new ArrayList<>();
        resultQuery.forEach(doctorsOfficeVo -> result.add(new DoctorsOfficeBo(doctorsOfficeVo)));
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
