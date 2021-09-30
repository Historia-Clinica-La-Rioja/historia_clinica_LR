package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class ConsultationCpoCeoIndicesStorageImpl implements ConsultationCpoCeoIndicesStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultationCpoCeoIndicesStorageImpl.class);

    private final OdontologyConsultationIndicesRepository odontologyConsultationIndicesRepository;

    public ConsultationCpoCeoIndicesStorageImpl(OdontologyConsultationIndicesRepository odontologyConsultationIndicesRepository) {
        this.odontologyConsultationIndicesRepository = odontologyConsultationIndicesRepository;
    }

    @Override
    public List<CpoCeoIndicesBo> getConsultationIndices(Integer patientId) {
        LOG.debug("Input parameters - > patientId {}", patientId);
        List<CpoCeoIndicesBo> result = this.odontologyConsultationIndicesRepository.getByPatientId(patientId)
                .stream()
                .map(this::mapToCpoCeoIndicesBo)
                .collect(Collectors.toList());
        LOG.debug("Output -> {}", result);
        return result;
    }

    private CpoCeoIndicesBo mapToCpoCeoIndicesBo(OdontologyConsultationIndices input) {
        LOG.debug("Input parameters -> input {}", input);
        CpoCeoIndicesBo result = new CpoCeoIndicesBo();
        result.setPermanentC(input.getPermanentC());
        result.setPermanentP(input.getPermanentP());
        result.setPermanentO(input.getPermanentO());
        result.setTemporaryC(input.getTemporaryC());
        result.setTemporaryE(input.getTemporaryE());
        result.setTemporaryO(input.getTemporaryO());
        result.setPermanentTeethPresent(input.getPermanentTeethPresent());
        result.setTemporaryTeethPresent(input.getTemporaryTeethPresent());
        result.setConsultationDate(input.getDate());
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public void saveIndices(Integer odontologyConsultationId, CpoCeoIndicesBo indices) {
        LOG.debug("Input parameters - > odontologyConsultationId {}, indices {}", odontologyConsultationId, indices);
        OdontologyConsultationIndices consultationIndices = new OdontologyConsultationIndices(odontologyConsultationId, indices);
        this.odontologyConsultationIndicesRepository.save(consultationIndices);
    }
}
