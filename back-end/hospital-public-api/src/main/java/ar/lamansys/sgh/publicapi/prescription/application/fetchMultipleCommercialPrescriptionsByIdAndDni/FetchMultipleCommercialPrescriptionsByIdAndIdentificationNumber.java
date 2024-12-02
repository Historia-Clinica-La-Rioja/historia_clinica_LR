package ar.lamansys.sgh.publicapi.prescription.application.fetchMultipleCommercialPrescriptionsByIdAndDni;

import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionV2Bo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionDosageBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionRequestException;
import ar.lamansys.sgh.publicapi.prescription.domain.utils.GroupOneCommercialMedication;
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

	public PrescriptionV2Bo run(String prescriptionId, String identificationNumber) {
		log.debug("Input parameters -> prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		assertUserPermissions();
		PrescriptionIdentifier prescriptionIdentifier = PrescriptionIdentifier.parse(prescriptionId);
		assertDomainNumber(prescriptionIdentifier.domain);
		PrescriptionV2Bo result = prescriptionStorage.getPrescriptionByIdAndDniV2(prescriptionIdentifier, identificationNumber)
				.orElseThrow(() -> new PrescriptionRequestException("Message", new Throwable()));
		correctQuantityValues(result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void correctQuantityValues(PrescriptionV2Bo prescription) {
		prescription.getPrescriptionLines().forEach(line -> correctQuantityValue(line.getPrescriptionDosage()));
	}

	private void correctQuantityValue(PrescriptionDosageBo lineDosage) {
		if (!GroupOneCommercialMedication.UNITS.contains(lineDosage.getQuantityUnit()) && lineDosage.getPackageQuantity() != null)
			lineDosage.setQuantity(Double.valueOf(lineDosage.getPackageQuantity()));
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
