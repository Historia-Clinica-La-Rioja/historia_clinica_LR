package net.pladema.internation.service.internment.impl;

import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.InternmentPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternmentPatientServiceImpl implements InternmentPatientService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public InternmentPatientServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    }

    @Override
    public List<BasicListedPatientBo> getInternmentPatients(Integer institutionId) {
        LOG.debug("Input parameters -> {}", institutionId);
        List<BasicListedPatientBo> result = internmentEpisodeRepository.findAllPatientsListedData(institutionId);
        LOG.debug("Output -> {}", result);
        return result;
    }

}
