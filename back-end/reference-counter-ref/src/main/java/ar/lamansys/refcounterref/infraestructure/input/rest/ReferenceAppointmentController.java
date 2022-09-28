package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.associatereferenceappointment.AssociateReferenceAppointment;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/reference-appointment")
@Tag(name = "Reference Appointment Controller", description = "Reference Appointment Controller")
public class ReferenceAppointmentController {

	private final AssociateReferenceAppointment associateReferenceAppointment;

	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public boolean associateReferenceAppointment(@PathVariable(name = "institutionId") Integer institutionId,
												 @RequestParam(name = "referenceId") Integer referenceId,
												 @RequestParam(name = "appointmentId") Integer appointmentId
	) {
		log.debug("Input parameters -> referenceId {}, appointmentId {}", referenceId, appointmentId);
		associateReferenceAppointment.run(referenceId, appointmentId);
		return Boolean.TRUE;
	}

}
