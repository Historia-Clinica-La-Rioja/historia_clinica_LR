package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstate.ChangePrescriptionState;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.BadPrescriptionIdFormatException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionDispenseException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionIdMatchException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionDispenseAccessDeniedException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.validators.ValidPrescriptionStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "PublicApi Recetas", description = "Public Api Digital Prescription Access")
@RequestMapping("/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}")
@RestController
public class DispensePrescriptionController {
	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final ChangePrescriptionState changePrescriptionState;

	private final PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	@Value("${prescription.domain.number}")
	private int domainNumber;

	@PutMapping
	public @ResponseBody ChangePrescriptionStateDto prescriptionDispense(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber,
			@RequestBody @ValidPrescriptionStatus ChangePrescriptionStateDto changePrescriptionLineDto
	) {

		if (!prescriptionPublicApiPermissions.canAccess()) {
			throw new PrescriptionDispenseAccessDeniedException();
		}

		log.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);

		try {
			var prescriptionIdentifier = PrescriptionIdentifier.parse(prescriptionId);
			assertDomainNumber(prescriptionIdentifier.domain);
			assertSamePrescriptionId(prescriptionId, changePrescriptionLineDto);
			changePrescriptionState.run(changePrescriptionLineDto, prescriptionIdentifier, identificationNumber);
		} catch (RuntimeException e) {
			throw new PrescriptionDispenseException(e.getMessage(), e);
		}

		log.debug(OUTPUT + "changePrescriptionLineDto {}", changePrescriptionLineDto);
		return changePrescriptionLineDto;
	}

	private void assertDomainNumber(String part) throws PrescriptionNotFoundException {
		if(Integer.parseInt(part) != domainNumber) {
			throw new PrescriptionNotFoundException("La receta no existe en el dominio.");
		}
	}



	private static void assertSamePrescriptionId(String prescriptionId, ChangePrescriptionStateDto changePrescriptionLineDto) throws PrescriptionIdMatchException {
		if(!prescriptionId.equals(changePrescriptionLineDto.getPrescriptionId())) {
			throw new PrescriptionIdMatchException("Los id de prescripción no coinciden.");
		}
	}
}
