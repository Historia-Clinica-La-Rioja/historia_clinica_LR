package ar.lamansys.odontology.application.odontogram.ports;

import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ToothIndicesBo;

import java.util.List;

public interface ToothIndicesStorage {

    void save(Integer patientId, List<ToothIndicesBo> teethIndices);

    List<ToothIndicesBo> getTeethIndices(Integer patientId);

	void deleteByPatientId(Integer patientId);

	CpoCeoIndicesBo computeIndices(Integer patientId, List<ConsultationDentalActionBo> dentalActions);

}
