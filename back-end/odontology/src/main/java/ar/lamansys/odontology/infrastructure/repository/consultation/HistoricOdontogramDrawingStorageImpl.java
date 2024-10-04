package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.application.odontogram.ports.HistoricOdontogramDrawingStorage;

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

	private final String REGISTRO_DE_ODONTOGRAMA_SCTID = "401281000221107";

	@Override
	public void save(HistoricOdontogramDrawing historicOdontogramDrawing) {
		historicOdontogramDrawingRepository.save(historicOdontogramDrawing);
	}

	@Override
	public Optional<HistoricOdontogramDrawing> getLastActiveHistoricOdontogramDrawingByPatientAndTooth(Integer patientId, String toothId) {
		log.debug("Input parameters -> patientId {}, toothId {}", patientId, toothId);
		Page<HistoricOdontogramDrawing> historicOdontogramDrawing = historicOdontogramDrawingRepository.getLastActiveHistoricOdontogramDrawingByPatientAndTooth(patientId, toothId, REGISTRO_DE_ODONTOGRAMA_SCTID, PageRequest.of(0, 1));
		Optional<HistoricOdontogramDrawing> result = historicOdontogramDrawing.getContent().stream().findFirst();
		log.debug("Output -> {}", result);
		return result;
	}
}
