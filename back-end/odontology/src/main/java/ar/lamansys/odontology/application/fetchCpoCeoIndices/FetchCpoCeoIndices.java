package ar.lamansys.odontology.application.fetchCpoCeoIndices;

import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchCpoCeoIndices {

    private static final Logger LOG = LoggerFactory.getLogger(FetchCpoCeoIndices.class);

    private final ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage;

    public FetchCpoCeoIndices(ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage) {
        this.consultationCpoCeoIndicesStorage = consultationCpoCeoIndicesStorage;
    }

    public List<CpoCeoIndicesBo> run(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        List<CpoCeoIndicesBo> consultationIndices = getFirstAndLastElements(consultationCpoCeoIndicesStorage.getConsultationIndices(patientId));
        List<CpoCeoIndicesBo> result = consultationIndices
                .stream()
                .map(this::setCpoIndex)
                .map(this::setCeoIndex)
                .collect(Collectors.toList());
        LOG.debug("Output -> {}", result);
        return result;
    }

    private List<CpoCeoIndicesBo> getFirstAndLastElements(List<CpoCeoIndicesBo> indicesList) {
        LOG.debug("Input parameters -> indicesList {}", indicesList);
        int size = indicesList.size();
        if (size <= 1)
            return indicesList;
        List<CpoCeoIndicesBo> result = Arrays.asList(indicesList.get(0), indicesList.get(size - 1));
        LOG.debug("Output -> {}", result);
        return result;
    }

    private CpoCeoIndicesBo setCeoIndex(CpoCeoIndicesBo indices) {
        LOG.debug("Input parameter -> indices {}", indices);
        Integer ceo = indices.getTemporaryC() + indices.getTemporaryE() + indices.getTemporaryO();
        indices.setCeoIndex(ceo);
        LOG.debug("Output -> {}", indices);
        return indices;
    }

    private CpoCeoIndicesBo setCpoIndex(CpoCeoIndicesBo indices) {
        LOG.debug("Input parameter -> indices {}", indices);
        Integer cpo = indices.getPermanentC() + indices.getPermanentP() + indices.getPermanentO();
        indices.setCpoIndex(cpo);
        LOG.debug("Output -> {}", indices);
        return indices;
    }

}
