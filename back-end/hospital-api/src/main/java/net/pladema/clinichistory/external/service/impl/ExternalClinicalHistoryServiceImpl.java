package net.pladema.clinichistory.external.service.impl;

import net.pladema.clinichistory.external.repository.ExternalClinicalHistoryRepository;
import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistory;
import net.pladema.clinichistory.external.service.ExternalClinicalHistoryService;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistorySummaryBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalClinicalHistoryServiceImpl implements ExternalClinicalHistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(ExternalClinicalHistoryServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    public static final String DEFAULT_VALUE = "-";

    private final ExternalClinicalHistoryRepository externalClinicalHistoryRepository;

    public ExternalClinicalHistoryServiceImpl(ExternalClinicalHistoryRepository externalClinicalHistoryRepository) {
        this.externalClinicalHistoryRepository = externalClinicalHistoryRepository;
    }

    @Override
    public List<ExternalClinicalHistorySummaryBo> getExternalClinicalHistory(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<ExternalClinicalHistorySummaryBo> result = externalClinicalHistoryRepository.getAllExternalClinicalHistory(patientId);
        LOG.debug("Output -> {}", result);
        return result;
    }

	@Override
	public Integer save(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
		LOG.debug("Input parameter -> externalClinicalHistoryBo {} ", externalClinicalHistoryBo);
		Integer result = externalClinicalHistoryRepository.save(mapToEntity(externalClinicalHistoryBo)).getId();
    	LOG.debug("Output -> result {} ", result);
		return result;
	}

	private ExternalClinicalHistory mapToEntity(ExternalClinicalHistoryBo externalClinicalHistoryBo) {
    	ExternalClinicalHistory result = new ExternalClinicalHistory();
    	result.setPatientDocumentNumber(externalClinicalHistoryBo.getPatientDocumentNumber());
    	result.setPatientDocumentType(externalClinicalHistoryBo.getPatientDocumentType());
    	result.setPatientGender(externalClinicalHistoryBo.getPatientGender());
    	result.setConsultationDate(externalClinicalHistoryBo.getConsultationDate());
    	result.setNotes(externalClinicalHistoryBo.getNotes());
		result.setProfessionalName(externalClinicalHistoryBo.getProfessionalName() != null ?
				externalClinicalHistoryBo.getProfessionalName() : DEFAULT_VALUE);
		result.setProfessionalSpecialty(externalClinicalHistoryBo.getProfessionalSpecialty() != null ?
				externalClinicalHistoryBo.getProfessionalSpecialty() : DEFAULT_VALUE);
		result.setInstitution(externalClinicalHistoryBo.getInstitution() != null ?
				externalClinicalHistoryBo.getInstitution() : DEFAULT_VALUE);
		return result;
	}
}
