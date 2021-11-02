package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;

import java.util.List;

public interface ToothIndicesStorage {

    void save(Integer patientId, List<ToothIndicesBo> teethIndices);

    List<ToothIndicesBo> getTeethIndices(Integer patientId);

}
