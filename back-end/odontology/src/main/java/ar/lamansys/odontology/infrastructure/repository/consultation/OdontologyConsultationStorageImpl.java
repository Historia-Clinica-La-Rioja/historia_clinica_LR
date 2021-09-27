package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationInfoBo;
import ar.lamansys.odontology.domain.consultation.ConsultationReasonBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
