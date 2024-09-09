package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.HistoricOdontogramDrawingStorage;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HistoricOdontogramDrawingStorageImpl implements HistoricOdontogramDrawingStorage {

	private final HistoricOdontogramDrawingRepository historicOdontogramDrawingRepository;

	@Override
	public void save(HistoricOdontogramDrawing historicOdontogramDrawing) {
		historicOdontogramDrawingRepository.save(historicOdontogramDrawing);
	}
}
