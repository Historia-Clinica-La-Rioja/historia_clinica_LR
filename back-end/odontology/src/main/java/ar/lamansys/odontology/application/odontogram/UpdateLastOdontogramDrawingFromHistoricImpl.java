package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UpdateLastOdontogramDrawingFromHistoricImpl implements UpdateLastOdontogramDrawingFromHistoric {

	private final OdontogramDrawingStorage odontogramDrawingStorage;

	@Transactional
	@Override
	public void run(Integer patientId, Integer healthConditionId) {
		odontogramDrawingStorage.updateOdontogramDrawingFromHistoric(patientId, healthConditionId);
	}
}
