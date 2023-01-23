package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoException;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.prescription.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public-api/prescriptions")
@Tag(name = "Public Api", description = "Public Api Digital Prescription Access")
public class PrescriptionAccessController {

	private static final Logger LOG = LoggerFactory.getLogger(PrescriptionAccessController.class);
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni;

	private final PrescriptionMapper prescriptionMapper;

	public PrescriptionAccessController(FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni, PrescriptionMapper prescriptionMapper) {
		this.fetchPrescriptionsByIdAndDni = fetchPrescriptionsByIdAndDni;
		this.prescriptionMapper = prescriptionMapper;
	}

	@GetMapping("/prescription/{prescriptionId}/identification/{identificationNumber}")
	public ResponseEntity<PrescriptionDto> getPrescription(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber
	) throws PrescriptionBoException {
		LOG.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);


		var result = prescriptionMapper.mapTo(fetchPrescriptionsByIdAndDni.run(prescriptionId, identificationNumber));
		if(result.getPrescriptionId() == null) {
			throw new PrescriptionBoException(PrescriptionBoEnumException.NOT_EXISTS_ID_OR_DNI, "NO SE ENCONTRÓ INFORMACIÓN SOBRE ESE DNI O ID DE RECETA");
		}
		LOG.debug(OUTPUT, result);

		return ResponseEntity.ok().body(result);
	}
}
