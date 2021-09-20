package ar.lamansys.odontology.domain.consultation;

import java.util.List;

public interface ConsultationCpoCeoIndicesStorage {

    List<CpoCeoIndicesBo> getConsultationIndices(Integer institutionId, Integer patientId);

}
