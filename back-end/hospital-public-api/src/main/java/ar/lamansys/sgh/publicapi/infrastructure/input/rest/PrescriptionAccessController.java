package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.application.changeprescriptionstate.ChangePrescriptionState;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.ChangePrescriptionStateDto;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.validators.ValidPrescriptionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoException;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.PrescriptionDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.prescription.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import java.util.Collections;

@RestController
@RequestMapping("/public-api/prescriptions")
@Tag(name = "Public Api", description = "Public Api Digital Prescription Access")
public class PrescriptionAccessController {

	private static final Logger LOG = LoggerFactory.getLogger(PrescriptionAccessController.class);
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni;

	private final ChangePrescriptionState changePrescriptionState;

	private final PrescriptionMapper prescriptionMapper;

	public PrescriptionAccessController(FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni, ChangePrescriptionState changePrescriptionState, PrescriptionMapper prescriptionMapper) {
		this.fetchPrescriptionsByIdAndDni = fetchPrescriptionsByIdAndDni;
		this.changePrescriptionState = changePrescriptionState;
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

	@PutMapping("/prescription/{prescriptionId}/identification/{identificationNumber}")
	public ResponseEntity<ChangePrescriptionStateDto> changePrescriptionLineState(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber,
			@RequestBody @ValidPrescriptionStatus ChangePrescriptionStateDto changePrescriptionLineDto
	) {
		LOG.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		assertSamePrescriptionId(prescriptionId, changePrescriptionLineDto);
		changePrescriptionState.run(changePrescriptionLineDto, prescriptionId, identificationNumber);
		return ResponseEntity.ok().body(changePrescriptionLineDto);
	}

	private void assertSamePrescriptionId(String prescriptionId, ChangePrescriptionStateDto changePrescriptionLineDto) {
		if(!prescriptionId.equals(changePrescriptionLineDto.getPrescriptionId().split("\\.")[1])) {
			throw new ConstraintViolationException("El identificador de receta no coincide con los de los renglones", Collections.emptySet());
		}
	}
}
