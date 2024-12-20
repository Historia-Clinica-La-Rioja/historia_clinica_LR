package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.odontogram.ports.HistoricOdontogramDrawingStorage;
import ar.lamansys.odontology.infrastructure.repository.consultation.HistoricOdontogramDrawing;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class GetLastActiveHistoricOdontogramDrawingImpl {

	private final HistoricOdontogramDrawingStorage historicOdontogramDrawingStorage;

	public Optional<HistoricOdontogramDrawing> run(Integer patientId, String toothId) {
		log.debug("Input parameters -> patientId {}, toothId {}", patientId, toothId);
		Optional<HistoricOdontogramDrawing> result = historicOdontogramDrawingStorage.getLastActiveHistoricOdontogramDrawingByPatientAndTooth(patientId, toothId);
		log.debug("Output -> {}", result);
		return result;
	}
}
