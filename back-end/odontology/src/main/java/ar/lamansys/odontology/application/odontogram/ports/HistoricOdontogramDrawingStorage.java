package ar.lamansys.odontology.application.odontogram.ports;

import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;

import java.util.Optional;

public interface HistoricOdontogramDrawingStorage {

	void save(HistoricOdontogramDrawing historicOdontogramDrawing);

	Optional<HistoricOdontogramDrawing> getLastActiveHistoricOdontogramDrawingByPatientAndTooth(Integer patientId, String toothId);
}
