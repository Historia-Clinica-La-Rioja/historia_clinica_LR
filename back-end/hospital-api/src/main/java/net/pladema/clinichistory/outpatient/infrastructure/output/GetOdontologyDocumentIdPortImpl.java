package net.pladema.clinichistory.outpatient.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import lombok.RequiredArgsConstructor;

import net.pladema.clinichistory.outpatient.application.port.output.GetOdontologyDocumentIdPort;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GetOdontologyDocumentIdPortImpl implements GetOdontologyDocumentIdPort {

	private final SharedOdontologyConsultationPort sharedOdontologyConsultationPort;

	@Override
	public Optional<Long> run(Integer healthConditionId) {
		return sharedOdontologyConsultationPort.getOdontologyDocumentId(healthConditionId);
	}
}
