package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;

public interface HistoricOdontogramDrawingStorage {

	void save(HistoricOdontogramDrawing historicOdontogramDrawing);
}
