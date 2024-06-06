package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.prescription.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionRequestException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionRequestAccessDeniedException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "PublicApi Recetas", description = "Public Api Digital Prescription Access")
@RequestMapping("/public-api/prescriptions/prescription/{prescriptionId}/identification/{identificationNumber}")
@RestController
public class FetchPrescriptionByIdController {

	private static final String OUTPUT = "Output -> {}";
	private static final String INPUT = "Input data -> ";

	private final FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni;

	private final PrescriptionMapper prescriptionMapper;

	private final PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	@Value("${prescription.domain.number}")
	private int domainNumber;

	@GetMapping
	public @ResponseBody PrescriptionDto prescriptionRequest(
			@PathVariable("prescriptionId") String prescriptionId,
			@PathVariable("identificationNumber") String identificationNumber) {

		if (!prescriptionPublicApiPermissions.canAccess()) {
			throw new PrescriptionRequestAccessDeniedException();
		}

		log.debug(INPUT + "prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);

		var prescriptionIdentifier = PrescriptionIdentifier.parse(prescriptionId);
		assertDomainNumber(prescriptionIdentifier.domain);
		try {
			var result = prescriptionMapper.mapTo(
					fetchPrescriptionsByIdAndDni.run(prescriptionIdentifier, identificationNumber)
							.orElseThrow()
			);
			log.debug(OUTPUT, result);
			return result;
		} catch (RuntimeException e) {
			throw new PrescriptionRequestException(e.getMessage(), e);
		}
	}

	private void assertDomainNumber(String part) throws PrescriptionNotFoundException {
		if(Integer.parseInt(part) != domainNumber) {
			throw new PrescriptionNotFoundException("No se encontró la receta.");
		}
	}

}
