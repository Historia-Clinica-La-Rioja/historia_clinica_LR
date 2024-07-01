package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.patient.application.fetchappointmentsdatabydni.FetchAppointmentsDataByDni;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public-api/patient/{identificationNumber}")
@Tag(name = "PublicApi Recetas de paciente", description = "Public Api Digital Prescription Access")
@Validated
public class PatientAppointmentsDataPublicController {

	private static final String INPUT = "Input data -> ";
	private static final Logger LOG = LoggerFactory.getLogger(PatientAppointmentsDataPublicController.class);

	private final FetchAppointmentsDataByDni fetchAppointmentsDataByDni;

	public PatientAppointmentsDataPublicController(FetchAppointmentsDataByDni fetchAppointmentsDataByDni) {
		this.fetchAppointmentsDataByDni = fetchAppointmentsDataByDni;
	}

	@GetMapping("/appointments")
	public ResponseEntity getAppointments(
			@PathVariable("identificationNumber") String identificationNumber,
			@RequestParam("identificationTypeId") Short identificationTypeId,
			@RequestParam("genderId") Short genderId,
			@RequestParam(name = "birthDate", required = false) String birthdate
	) {
		LOG.debug(INPUT + "identificationNumber {}", identificationNumber);
		return ResponseEntity.ok().body(fetchAppointmentsDataByDni.run(identificationNumber, identificationTypeId, genderId, birthdate));
	}
}