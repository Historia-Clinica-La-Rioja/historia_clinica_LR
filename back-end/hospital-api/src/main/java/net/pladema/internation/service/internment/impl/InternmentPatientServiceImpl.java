package net.pladema.internation.service.internment.impl;

import net.pladema.internation.repository.internment.InternmentEpisodeRepository;
import net.pladema.internation.repository.internment.domain.processepisode.InternmentEpisodeProcessVo;
import net.pladema.internation.service.internment.InternmentPatientService;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeProcessBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternmentPatientServiceImpl implements InternmentPatientService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientServiceImpl.class);

    private static final String INPUT_PARAMETERS_INSTITUTION_ID = "Input parameters -> institutionId {}";
    private static final String LOGGING_OUTPUT = "Output -> {}";

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public InternmentPatientServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    }

    @Override
    public List<BasicListedPatientBo> getInternmentPatients(Integer institutionId) {
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<BasicListedPatientBo> result = internmentEpisodeRepository.findAllPatientsListedData(institutionId);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<InternmentEpisodeBo> getAllInternmentPatient(Integer institutionId) {
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<InternmentEpisodeBo> result = internmentEpisodeRepository.getAllInternmentPatient(institutionId);
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public InternmentEpisodeProcessBo internmentEpisodeInProcess(Integer institutionId, Integer patientId) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        InternmentEpisodeProcessBo result = new InternmentEpisodeProcessBo(null, false);
        Optional<InternmentEpisodeProcessVo> resultQuery = internmentEpisodeRepository.internmentEpisodeInProcess(patientId);
        resultQuery.ifPresent(rq -> {
            result.setInProgress(true);
            if (rq.getInstitutionId().equals(institutionId))
                result.setId(rq.getId());
        });
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

}
