package net.pladema.clinichistory.requests.medicationrequests.application.port.output;

import java.util.UUID;

public interface MedicationRequestPort {

	UUID getMedicationRequestUUIDById(Integer medicationRequestId);

}
