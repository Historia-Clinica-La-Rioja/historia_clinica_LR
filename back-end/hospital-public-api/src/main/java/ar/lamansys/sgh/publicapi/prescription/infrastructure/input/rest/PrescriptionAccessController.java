package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest;

import java.util.regex.Pattern;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstate.ChangePrescriptionState;
import ar.lamansys.sgh.publicapi.prescription.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.BadPrescriptionIdFormatException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionDispenseException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionIdMatchException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionRequestException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.validators.ValidPrescriptionStatus;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@Tag(name = "PublicApi Recetas", description = "Public Api Digital Prescription Access")
@RequestMapping("/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}")
public class PrescriptionAccessController {
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private static final String ID_DIVIDER = "-";

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

	@GetMapping
	public @ResponseBody PrescriptionDto prescriptionRequest(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber
	) throws BadPrescriptionIdFormatException, PrescriptionNotFoundException {
		log.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		var parts = prescriptionId.split(ID_DIVIDER);
		assertFormatPrescriptionId(parts);
		assertDomainNumber(parts[0]);
		try {
			var result = prescriptionMapper.mapTo(fetchPrescriptionsByIdAndDni.run(prescriptionId, identificationNumber));
			log.debug(OUTPUT, result);
			return result;
		} catch (RuntimeException e) {
			throw new PrescriptionRequestException(e.getMessage(), e);
		}
	}

	@PutMapping
	public @ResponseBody ChangePrescriptionStateDto prescriptionDispense(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber,
			@RequestBody @ValidPrescriptionStatus ChangePrescriptionStateDto changePrescriptionLineDto
	) throws BadPrescriptionIdFormatException, PrescriptionIdMatchException {

		log.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		var parts = prescriptionId.split(ID_DIVIDER);
		assertSamePrescriptionId(prescriptionId, changePrescriptionLineDto);
		assertFormatPrescriptionId(parts);
		assertDomainNumber(parts[0]);
		try {
			changePrescriptionState.run(changePrescriptionLineDto, prescriptionId, identificationNumber);
		} catch (RuntimeException e) {
			throw new PrescriptionDispenseException(e.getMessage(), e);
		}
		return changePrescriptionLineDto;
	}

	private void assertDomainNumber(String part) throws BadPrescriptionIdFormatException {
		if(Integer.parseInt(part) != domainNumber) {
			throw new BadPrescriptionIdFormatException();
		}
	}

	private static void assertFormatPrescriptionId(String[] parts) throws BadPrescriptionIdFormatException {
		Pattern pattern = Pattern.compile("\\d+");
		if(parts.length != 2 || !pattern.matcher(parts[0]).matches() || !pattern.matcher(parts[1]).matches()) {
			throw new BadPrescriptionIdFormatException();
		}
	}

	private static void assertSamePrescriptionId(String prescriptionId, ChangePrescriptionStateDto changePrescriptionLineDto) throws PrescriptionIdMatchException {
		if(!prescriptionId.equals(changePrescriptionLineDto.getPrescriptionId())) {
			throw new PrescriptionIdMatchException();
		}
	}
}
