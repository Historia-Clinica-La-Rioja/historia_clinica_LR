package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.nursing.domain.NursingConsultationInfoBo;
import ar.lamansys.nursing.application.port.NursingConsultationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NursingConsultationStorageImpl implements NursingConsultationStorage {

    private static final Logger LOG = LoggerFactory.getLogger(NursingConsultationStorageImpl.class);

    private final NursingConsultationRepository nursingConsultationRepository;

    public NursingConsultationStorageImpl(NursingConsultationRepository nursingConsultationRepository) {
        this.nursingConsultationRepository = nursingConsultationRepository;
    }

    @Override
    public Integer save(NursingConsultationInfoBo nursingConsultationInfoBo) {
        LOG.debug("Input parameters -> nursingConsultationInfoBo {}", nursingConsultationInfoBo);

        NursingConsultation nursingConsultation = new NursingConsultation(nursingConsultationInfoBo);
        Integer nursingConsultationId = nursingConsultationRepository.save(nursingConsultation).getId();

        LOG.debug("Output -> {}", nursingConsultationId);

        return nursingConsultationId;
    }

}
