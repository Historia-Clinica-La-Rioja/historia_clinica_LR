package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class ConsultationCpoCeoIndicesStorageImpl implements ConsultationCpoCeoIndicesStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultationCpoCeoIndicesStorageImpl.class);

    private final OdontologyConsultationIndicesRepository odontologyConsultationIndicesRepository;

    public ConsultationCpoCeoIndicesStorageImpl(OdontologyConsultationIndicesRepository odontologyConsultationIndicesRepository) {
        this.odontologyConsultationIndicesRepository = odontologyConsultationIndicesRepository;
    }

    @Override
    public List<CpoCeoIndicesBo> getConsultationIndices(Integer institutionId, Integer patientId) {
        return new ArrayList<>();
    }

    @Override
    public void saveIndices(Integer odontologyConsultationId, CpoCeoIndicesBo indices) {
        LOG.debug("Input parameters - > odontologyConsultationId {}, indices {}", odontologyConsultationId, indices);
        OdontologyConsultationIndices consultationIndices = new OdontologyConsultationIndices(odontologyConsultationId, indices);
        this.odontologyConsultationIndicesRepository.save(consultationIndices);
    }
}
