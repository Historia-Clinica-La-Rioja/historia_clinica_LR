package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultationCpoCeoIndicesStorageMockImpl implements ConsultationCpoCeoIndicesStorage {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultationCpoCeoIndicesStorageMockImpl.class);

    @Override
    public List<CpoCeoIndicesBo> getConsultationIndices(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        List<CpoCeoIndicesBo> result = new ArrayList<>();
        if (patientId % 2 == 0) {
            result.add(new CpoCeoIndicesBo(1, 2, 3,
                    1, 2, 3,
                    10, 10,
                    30, 18,
                    LocalDateTime.of(2020, 9, 15, 0, 0)));
            result.add(new CpoCeoIndicesBo(5, 3, 4,
                    1, 3, 3,
                    12, 15,
                    28, 16,
                    LocalDateTime.of(2021, 7, 20, 0, 0)));
        }
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public void saveIndices(Integer patientId, CpoCeoIndicesBo indices) { }

}
