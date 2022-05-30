package net.pladema.clinichistory.hospitalization.service.impl;

import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.processepisode.InternmentEpisodeProcessVo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import net.pladema.clinichistory.hospitalization.service.domain.BasicListedPatientBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeProcessBo;

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

	private final InternmentEpisodeService internmentEpisodeService;

    public InternmentPatientServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository, InternmentEpisodeService internmentEpisodeService) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    	this.internmentEpisodeService = internmentEpisodeService;
	}

    @Override
    public List<BasicListedPatientBo> getInternmentPatients(Integer institutionId) {
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<BasicListedPatientBo> result = internmentEpisodeRepository.findAllPatientsListedData(institutionId);
		result.forEach(i -> internmentEpisodeService.getIntermentSummary(i.getInternmentId()).ifPresent(e-> i.setDocumentsSummary(e.getDocuments())));
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<InternmentEpisodeBo> getAllInternmentPatient(Integer institutionId) {
        LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID, institutionId);
        List<InternmentEpisodeBo> result = internmentEpisodeRepository.getAllInternmentPatient(institutionId);
		result.forEach(i -> internmentEpisodeService.getIntermentSummary(i.getId()).ifPresent(e-> i.setDocumentsSummary(e.getDocuments())));
		LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public InternmentEpisodeProcessBo internmentEpisodeInProcess(Integer institutionId, Integer patientId) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        InternmentEpisodeProcessBo result = new InternmentEpisodeProcessBo(null, false, false);
        List<InternmentEpisodeProcessVo> resultQuery = internmentEpisodeRepository.internmentEpisodeInProcess(patientId);
		if(!resultQuery.isEmpty()) {
			result.setInProgress(true);
			result.setPatientHospitalized(internmentEpisodeRepository.isPatientHospitalized(patientId));
			resultQuery.forEach(rq -> {
				if (rq.getInstitutionId().equals(institutionId))
					result.setId(rq.getId());
			});
		}
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

	@Override
	public Optional<Integer> getInternmentEpisodeIdInProcess(Integer institutionId, Integer patientId) {
		LOG.debug("Input parameters -> patientId {}", patientId);
		Optional<Integer> result = Optional.empty();
		InternmentEpisodeProcessVo internmentEpisode = new InternmentEpisodeProcessVo(null, institutionId);
		List<InternmentEpisodeProcessVo> resultQuery = internmentEpisodeRepository.internmentEpisodeInProcess(patientId);
		if(!resultQuery.isEmpty()) {
			resultQuery.forEach(rq -> {
				if (rq.getInstitutionId().equals(institutionId)) {
					internmentEpisode.setId(rq.getId());
				}
			});
			if (internmentEpisode.getId() != null) {
				result = Optional.of(internmentEpisode.getId());
			} else {
				result = Optional.of(resultQuery.stream().findFirst().get().getId());
			}
		}
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}


}
