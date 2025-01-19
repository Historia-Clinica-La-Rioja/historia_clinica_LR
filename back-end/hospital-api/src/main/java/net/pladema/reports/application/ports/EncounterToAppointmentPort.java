package net.pladema.reports.application.ports;

import java.util.Optional;

public interface EncounterToAppointmentPort {
	public Optional<Long> encounterToDocumentId(Integer encounterId, Short sourceTypeId);

	boolean supportsSourceType(Short sourceTypeId);
}
