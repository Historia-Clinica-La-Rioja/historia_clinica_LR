package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.application.odontogram.ports.OdontogramDrawingStorage;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UpdateLastOdontogramDrawingFromHistoricImpl {

	private final OdontogramDrawingStorage odontogramDrawingStorage;

	@Transactional
	public void run(Integer patientId, Integer healthConditionId) {
		odontogramDrawingStorage.updateOdontogramDrawingFromHistoric(patientId, healthConditionId);
	}
}
