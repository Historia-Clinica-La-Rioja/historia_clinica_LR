package ar.lamansys.odontology.application.odontogram.ports;

import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;

import java.util.List;

public interface OdontogramDrawingStorage {

    void save(Integer patientId, List<ToothDrawingsBo> teethDrawings);

	void updateConsultationId(Integer consultationId, String toothId, Integer patientId);

    List<ToothDrawingsBo> getDrawings(Integer patientId);

	void deleteByPatientId(Integer patientId);

	void updateOdontogramDrawingFromHistoric(Integer patientId, Integer healthConditionId);

}
