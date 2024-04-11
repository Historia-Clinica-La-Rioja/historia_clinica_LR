package net.pladema.reports.infrastructure.output;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultation;
import net.pladema.reports.application.ports.EncounterToAppointmentPort;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EncounterToAppointmentStorage implements EncounterToAppointmentPort {

	private final OutpatientConsultationRepository outpatientConsultationRepository;

	@Override
	public Optional<Long> encounterToDocumentId(Integer encounterId, Short sourceTypeId) {
		if (!isOutpatient(sourceTypeId)) throw new RuntimeException();
		return outpatientConsultationRepository
		.findById(encounterId)
		.map(OutpatientConsultation::getDocumentId);
	}

	@Override
	public boolean supportsSourceType(Short sourceTypeId) {
		return isOutpatient(sourceTypeId);
	}

	private boolean isOutpatient(Short sourceTypeId) {
		return Objects.equals(sourceTypeId, SourceType.OUTPATIENT);
	}
}
