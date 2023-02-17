package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import java.util.Collections;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.application.changeprescriptionstate.ChangePrescriptionState;
import ar.lamansys.sgh.publicapi.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoEnumException;
import ar.lamansys.sgh.publicapi.domain.exceptions.PrescriptionBoException;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.validators.ValidPrescriptionStatus;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.mapper.prescription.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public-api/prescriptions")
@Tag(name = "Public Api", description = "Public Api Digital Prescription Access")
@Validated
public class PrescriptionAccessController {

	private static final Logger LOG = LoggerFactory.getLogger(PrescriptionAccessController.class);
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private static final String ID_DIVIDER = "\\.";

	private final FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni;

	private final ChangePrescriptionState changePrescriptionState;

	private final PrescriptionMapper prescriptionMapper;

	@Value("${prescription.domain.number}")
	private int domainNumber;

	public PrescriptionAccessController(FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni, ChangePrescriptionState changePrescriptionState, PrescriptionMapper prescriptionMapper) {
		this.fetchPrescriptionsByIdAndDni = fetchPrescriptionsByIdAndDni;
		this.changePrescriptionState = changePrescriptionState;
		this.prescriptionMapper = prescriptionMapper;
	}

	@GetMapping("/prescription/{prescriptionId}/identification/{identificationNumber}")
	public ResponseEntity getPrescription(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber
	) {
		LOG.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		try {
			var parts = prescriptionId.split(ID_DIVIDER);

			assertFormatPrescriptionId(parts);
			assertDomainNumber(parts[0]);

			var result = prescriptionMapper.mapTo(fetchPrescriptionsByIdAndDni.run(prescriptionId, identificationNumber));

			LOG.debug(OUTPUT, result);
			return ResponseEntity.ok().body(result);
		}
		catch (PrescriptionBoException exc) {
			LOG.debug(OUTPUT, exc.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
		}
	}

	private void assertDomainNumber(String part) throws PrescriptionBoException {
		if(Integer.parseInt(part) != domainNumber) {
			throw new PrescriptionBoException(PrescriptionBoEnumException.NOT_EXISTS_ID_OR_DNI, "No se encontró información sobre ese dni o id de receta");
		}
	}

	private void assertFormatPrescriptionId(String[] parts) throws PrescriptionBoException{
		Pattern pattern = Pattern.compile("\\d+");
		if(parts.length != 2 || !pattern.matcher(parts[0]).matches() || !pattern.matcher(parts[1]).matches()) {
			throw new PrescriptionBoException(PrescriptionBoEnumException.WRONG_FORMAT_ID, "El id de receta no tiene el formato correcto.");
		}
	}

	@PutMapping("/prescription/{prescriptionId}/identification/{identificationNumber}")
	public ResponseEntity changePrescriptionLineState(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber,
			@RequestBody @ValidPrescriptionStatus ChangePrescriptionStateDto changePrescriptionLineDto
	) {

		LOG.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		try {
			assertSamePrescriptionId(prescriptionId, changePrescriptionLineDto);
			assertFormatPrescriptionId(prescriptionId.split(ID_DIVIDER));
			assertDomainNumber(prescriptionId.split(ID_DIVIDER)[0]);
			changePrescriptionState.run(changePrescriptionLineDto, prescriptionId, identificationNumber);
		}
		catch (ConstraintViolationException | PrescriptionBoException exc) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exc.getMessage());
		}
		return ResponseEntity.ok().body(changePrescriptionLineDto);
	}

	private void assertSamePrescriptionId(String prescriptionId, ChangePrescriptionStateDto changePrescriptionLineDto) {
		if(!prescriptionId.equals(changePrescriptionLineDto.getPrescriptionId())) {
			throw new ConstraintViolationException("El identificador de receta no coincide con los de los renglones", Collections.emptySet());
		}
	}
}
