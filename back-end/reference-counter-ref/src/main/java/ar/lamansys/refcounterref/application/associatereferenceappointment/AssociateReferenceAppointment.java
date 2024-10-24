package ar.lamansys.refcounterref.application.associatereferenceappointment;

import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssociateReferenceAppointment {

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	public void run(Integer referenceId, Integer appointmentId, Boolean isProtected) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}, isProtected {}", referenceId, appointmentId, isProtected);
		referenceAppointmentStorage.save(referenceId, appointmentId, isProtected);
	}

}
