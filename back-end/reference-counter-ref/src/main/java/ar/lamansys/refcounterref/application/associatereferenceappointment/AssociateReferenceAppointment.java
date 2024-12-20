package ar.lamansys.refcounterref.application.associatereferenceappointment;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssociateReferenceAppointment {

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	private final ReferenceStorage referenceStorage;

	public void run(Integer referenceId, Integer appointmentId, Boolean isProtected, Integer institutionId) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}, isProtected {}, institutionId {}", referenceId, appointmentId, isProtected, institutionId);
		referenceAppointmentStorage.save(referenceId, appointmentId, isProtected);
		referenceStorage.updateDestinationInstitution(referenceId, institutionId);
	}

}
