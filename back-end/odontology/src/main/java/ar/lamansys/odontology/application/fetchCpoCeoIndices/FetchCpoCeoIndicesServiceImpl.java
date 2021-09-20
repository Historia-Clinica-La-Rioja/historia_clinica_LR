package ar.lamansys.odontology.application.fetchCpoCeoIndices;

import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchCpoCeoIndicesServiceImpl implements FetchCpoCeoIndicesService {

    private static final Logger LOG = LoggerFactory.getLogger(FetchCpoCeoIndicesServiceImpl.class);

    private final ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage;

    public FetchCpoCeoIndicesServiceImpl(ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage) {
        this.consultationCpoCeoIndicesStorage = consultationCpoCeoIndicesStorage;
    }

    @Override
    public List<CpoCeoIndicesBo> run(Integer institutionId, Integer patientId) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        List<CpoCeoIndicesBo> result = consultationCpoCeoIndicesStorage.getConsultationIndices(institutionId, patientId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
