package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.application.fetchprescriptionsdatabydni.FetchPrescriptionsDataByDni;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public-api/patient/{identificationNumber}")
@Tag(name = "PublicApi Recetas de paciente", description = "Public Api Digital Prescription Access")
@Validated
public class PatientPrescriptionsDataController {

	private static final String INPUT = "Input data -> ";
	private static final Logger LOG = LoggerFactory.getLogger(PatientPrescriptionsDataController.class);

	private final FetchPrescriptionsDataByDni fetchPrescriptionsDataByDni;

	public PatientPrescriptionsDataController(FetchPrescriptionsDataByDni fetchPrescriptionsDataByDni) {
		this.fetchPrescriptionsDataByDni = fetchPrescriptionsDataByDni;
	}

	@GetMapping("/prescriptions")
	public ResponseEntity getPrescriptions(
			@PathVariable("identificationNumber") String identificationNumber
	) {
		LOG.debug(INPUT + "identificationNumber {}", identificationNumber);
		return ResponseEntity.ok().body(fetchPrescriptionsDataByDni.run(identificationNumber));
	}
}
