package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;

import org.springframework.stereotype.Service;

@Service
public interface SaveHistoricOdontogramDrawing {

	void run(HistoricOdontogramDrawing historicOdontogramDrawing);
}
