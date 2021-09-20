package ar.lamansys.odontology.application.fetchCpoCeoIndices;

import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;

import java.util.List;

public interface FetchCpoCeoIndicesService {

    List<CpoCeoIndicesBo> run(Integer institutionId, Integer patientId);

}
