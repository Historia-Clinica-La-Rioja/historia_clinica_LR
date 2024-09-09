package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.consultation.HistoricOdontogramDrawingStorage;

import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SaveHistoricOdontogramDrawingImpl implements SaveHistoricOdontogramDrawing {

	private final HistoricOdontogramDrawingStorage historicOdontogramDrawingStorage;

	@Override
	public void run(HistoricOdontogramDrawing historicOdontogramDrawing) {
		historicOdontogramDrawingStorage.save(historicOdontogramDrawing);
	}
}
