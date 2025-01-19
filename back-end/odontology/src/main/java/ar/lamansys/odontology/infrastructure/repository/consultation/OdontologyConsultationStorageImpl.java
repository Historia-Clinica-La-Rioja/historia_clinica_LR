package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.application.odontogram.ports.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationInfoBo;
import ar.lamansys.odontology.domain.consultation.ConsultationReasonBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdontologyConsultationStorageImpl implements OdontologyConsultationStorage {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyConsultationStorageImpl.class);

    private final OdontologyConsultationRepository odontologyConsultationRepository;

    private final OdontologyReasonRepository odontologyReasonRepository;

    private final OdontologyConsultationReasonRepository odontologyConsultationReasonRepository;

    public OdontologyConsultationStorageImpl(OdontologyConsultationRepository odontologyConsultationRepository,
                                             OdontologyReasonRepository odontologyReasonRepository,
                                             OdontologyConsultationReasonRepository odontologyConsultationReasonRepository) {
        this.odontologyConsultationRepository = odontologyConsultationRepository;
        this.odontologyReasonRepository = odontologyReasonRepository;
        this.odontologyConsultationReasonRepository = odontologyConsultationReasonRepository;
    }

    @Override
    public Integer save(ConsultationInfoBo consultationInfo) {
        LOG.debug("Input parameters -> consultationInfo {}", consultationInfo);
        OdontologyConsultation odontologyConsultation = new OdontologyConsultation(consultationInfo);
        Integer odontologyConsultationId = odontologyConsultationRepository.save(odontologyConsultation).getId();
        if (consultationInfo.getReasons() != null)
            saveReasons(consultationInfo.getReasons(), odontologyConsultationId);
        LOG.debug("Output -> {}", odontologyConsultationId);
        return odontologyConsultationId;
    }

    private void saveReasons(List<ConsultationReasonBo> reasons, Integer odontologyConsultationId) {
        reasons.forEach(reason -> {
            odontologyReasonRepository.save(new OdontologyReason(reason));
            odontologyConsultationReasonRepository.save(new OdontologyConsultationReason(odontologyConsultationId, reason.getSctid()));
        });
    }

    @Override
    public boolean hasPreviousConsultations(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        boolean result = odontologyConsultationRepository.patientHasPreviousConsultations(patientId);
        LOG.debug("Output -> {}", result);
        return result;
    }

	@Override
	public List<Integer> getOdontologyConsultationIdsFromPatients(List<Integer> patients) {
		LOG.debug("Input parameter -> patients {}", patients);
		List<Integer> result = odontologyConsultationRepository.getOdontologyConsultationIdFromPatients(patients);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<OdontologyConsultation> findAllById(List<Integer> ids) {
		LOG.debug("Input parameter -> OdontologyConsultationIds {}", ids);
		List<OdontologyConsultation> result = odontologyConsultationRepository.findAllById(ids);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public OdontologyConsultation getLastByPatientId(Integer patientId) {
		LOG.debug("Input parameter -> patientId {}", patientId);
		List<OdontologyConsultation> partialResult = odontologyConsultationRepository.getLastOdontologyConsultationFromPatient(patientId);
		OdontologyConsultation result = partialResult.get(partialResult.size()-1);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<Integer> getPatientMedicalCoverageId(Integer id) {
		LOG.debug("Input parameter -> id {}", id);
		Optional<Integer> result = odontologyConsultationRepository.getPatientMedicalCoverageId(id);
		LOG.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<Long> getOdontologyDocumentId(Integer healthConditionId) {
		LOG.debug("Input parameter -> healthConditionId {}", healthConditionId);
		return odontologyConsultationRepository.getOdontologyDocumentId(healthConditionId);
	}
}
