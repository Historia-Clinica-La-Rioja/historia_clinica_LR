package net.pladema.internation.service.internment.impl;

import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.InternmentPatientService;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternmentPatientServiceImpl implements InternmentPatientService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientServiceImpl.class);
    
    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public InternmentPatientServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    }

    @Override
    public List<BasicListedPatientBo> getInternmentPatients(Integer institutionId) {
        LOG.debug("Input parameters -> {}", institutionId);
        List<BasicListedPatientBo> result = internmentEpisodeRepository.findAllPatientsListedData(institutionId);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<InternmentEpisodeBo> getAllInternmentPatient(Integer institutionId) {
        LOG.debug("Input parameters -> {}", institutionId);
        List<InternmentEpisodeBo> result = internmentEpisodeRepository.getAllInternmentPatient(institutionId);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

}
