package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.HistoricOdontogramDrawingStorage;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class HistoricOdontogramDrawingStorageImpl implements HistoricOdontogramDrawingStorage {

	private final HistoricOdontogramDrawingRepository historicOdontogramDrawingRepository;

	@Override
	public void save(HistoricOdontogramDrawing historicOdontogramDrawing) {
		historicOdontogramDrawingRepository.save(historicOdontogramDrawing);
	}

	@Override
	public Optional<HistoricOdontogramDrawing> getLastActiveHistoricOdontogramDrawingByPatientAndTooth(Integer patientId, String toothId) {
		log.debug("Input parameters -> patientId {}, toothId {}", patientId, toothId);
		Page<HistoricOdontogramDrawing> historicOdontogramDrawing = historicOdontogramDrawingRepository.getLastActiveHistoricOdontogramDrawingByPatientAndTooth(patientId, toothId, PageRequest.of(0, 2));
		Optional<HistoricOdontogramDrawing> result = historicOdontogramDrawing.getContent().stream().findFirst();
		log.debug("Output -> {}", result);
		return result;
	}
}
