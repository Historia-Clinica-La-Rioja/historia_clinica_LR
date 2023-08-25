package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest;

import org.springframework.beans.factory.annotation.Value;
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
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.BadPrescriptionIdFormatException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionDispenseException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionIdMatchException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionRequestException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionDispenseAccessDeniedException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionRequestAccessDeniedException;
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

	private final FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni;

	private final ChangePrescriptionState changePrescriptionState;

	private final PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	private final PrescriptionMapper prescriptionMapper;

	@Value("${prescription.domain.number}")
	private int domainNumber;

	public PrescriptionAccessController(
			PrescriptionPublicApiPermissions prescriptionPublicApiPermissions,
			FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni,
			ChangePrescriptionState changePrescriptionState,
			PrescriptionMapper prescriptionMapper
	) {
		this.prescriptionPublicApiPermissions = prescriptionPublicApiPermissions;
		this.fetchPrescriptionsByIdAndDni = fetchPrescriptionsByIdAndDni;
		this.changePrescriptionState = changePrescriptionState;
		this.prescriptionMapper = prescriptionMapper;
	}

	@GetMapping
	public @ResponseBody PrescriptionDto prescriptionRequest(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber
	) throws BadPrescriptionIdFormatException, PrescriptionNotFoundException {

		if (!prescriptionPublicApiPermissions.canAccess()) {
			throw new PrescriptionRequestAccessDeniedException();
		}
		log.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);

		var prescriptionIdentifier = PrescriptionIdentifier.parse(prescriptionId);
		assertDomainNumber(prescriptionIdentifier.domain);

		try {
			var result = prescriptionMapper.mapTo(
					fetchPrescriptionsByIdAndDni.run(prescriptionIdentifier, identificationNumber)
							.orElseThrow(PrescriptionNotFoundException::new)
			);
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
	) throws BadPrescriptionIdFormatException, PrescriptionIdMatchException, PrescriptionNotFoundException {

		if (!prescriptionPublicApiPermissions.canAccess()) {
			throw new PrescriptionDispenseAccessDeniedException();
		}

		log.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);

		var prescriptionIdentifier = PrescriptionIdentifier.parse(prescriptionId);
		assertDomainNumber(prescriptionIdentifier.domain);
		assertSamePrescriptionId(prescriptionId, changePrescriptionLineDto);

		try {
			changePrescriptionState.run(changePrescriptionLineDto, prescriptionIdentifier, identificationNumber);
		} catch (RuntimeException e) {
			throw new PrescriptionDispenseException(e.getMessage(), e);
		}
		return changePrescriptionLineDto;
	}

	private void assertDomainNumber(String part) throws PrescriptionNotFoundException {
		if(Integer.parseInt(part) != domainNumber) {
			throw new PrescriptionNotFoundException();
		}
	}



	private static void assertSamePrescriptionId(String prescriptionId, ChangePrescriptionStateDto changePrescriptionLineDto) throws PrescriptionIdMatchException {
		if(!prescriptionId.equals(changePrescriptionLineDto.getPrescriptionId())) {
			throw new PrescriptionIdMatchException();
		}
	}
}
