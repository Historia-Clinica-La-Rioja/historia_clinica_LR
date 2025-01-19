package net.pladema.clinichistory.outpatient.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.outpatient.application.port.output.UpdateLastOdontogramDrawingFromHistoricPort;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateLastOdontogramDrawingFromHistoricPortImpl implements UpdateLastOdontogramDrawingFromHistoricPort {

	private final SharedOdontologyConsultationPort sharedOdontologyConsultationPort;

	@Override
	public void run(Integer patientId, Integer healthConditionId) {
		sharedOdontologyConsultationPort.updateOdontogramDrawingFromHistoric(patientId, healthConditionId);
	}
}
