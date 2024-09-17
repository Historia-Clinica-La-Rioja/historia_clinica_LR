package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.odontogram.ports.HistoricOdontogramDrawingStorage;
import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SaveHistoricOdontogramDrawingImpl {

	private final HistoricOdontogramDrawingStorage historicOdontogramDrawingStorage;

	public void run(HistoricOdontogramDrawing historicOdontogramDrawing) {
		historicOdontogramDrawingStorage.save(historicOdontogramDrawing);
	}
}
