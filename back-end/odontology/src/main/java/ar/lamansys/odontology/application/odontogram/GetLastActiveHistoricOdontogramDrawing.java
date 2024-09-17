package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;

import java.util.Optional;

public interface GetLastActiveHistoricOdontogramDrawing {

	Optional<HistoricOdontogramDrawing> run(Integer patientId, String toothId);
}
