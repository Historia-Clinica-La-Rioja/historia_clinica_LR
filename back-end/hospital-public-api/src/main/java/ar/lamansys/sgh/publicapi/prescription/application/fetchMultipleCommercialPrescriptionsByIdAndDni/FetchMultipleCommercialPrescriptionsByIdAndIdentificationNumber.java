package ar.lamansys.sgh.publicapi.prescription.application.fetchMultipleCommercialPrescriptionsByIdAndDni;

import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.MultipleCommercialPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionRequestAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber {

	@Value("${prescription.domain.number}")
	private int domainNumber;

	private final PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	private final PrescriptionStorage prescriptionStorage;

	public MultipleCommercialPrescriptionBo run(String prescriptionId, String identificationNumber) {
		log.debug("Input parameters -> prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		assertUserPermissions();
		PrescriptionIdentifier prescriptionIdentifier = PrescriptionIdentifier.parse(prescriptionId);
		assertDomainNumber(prescriptionIdentifier.domain);
		MultipleCommercialPrescriptionBo result = prescriptionStorage.getMultipleCommercialPrescriptionByIdAndIdentificationNumber(prescriptionIdentifier, identificationNumber);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertDomainNumber(String domain) {
		if (Integer.parseInt(domain) != domainNumber)
			throw new PrescriptionNotFoundException("No se encontró la receta.");
	}

	private void assertUserPermissions() {
		if (!prescriptionPublicApiPermissions.canAccess())
			throw new PrescriptionRequestAccessDeniedException();
	}

}
