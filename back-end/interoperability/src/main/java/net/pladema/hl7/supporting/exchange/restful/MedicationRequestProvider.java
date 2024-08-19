package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hl7.dataexchange.exceptions.PrescriptionException;
import net.pladema.hl7.dataexchange.exceptions.PrescriptionExceptionEnum;
import net.pladema.hl7.dataexchange.medications.MedicationRequestResource;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.documents.BundleResource;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/fhir")
@Conditional(InteroperabilityCondition.class)
@Slf4j
public class MedicationRequestProvider implements IResourceProvider {

	private final MedicationRequestResource medicationRequestResource;

	private final BundleResource bundleResource;

	@Value("${prescription.domain.number}")
	private int domainNumber;

	public MedicationRequestProvider(MedicationRequestResource medicationRequestResource, BundleResource bundleResource) {
		this.medicationRequestResource = medicationRequestResource;
		this.bundleResource = bundleResource;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return MedicationRequest.class;
	}

	@GetMapping(value = "/MedicationRequest")
	@Search
	public Bundle getMedicationRequest(
			@RequiredParam(name = MedicationRequest.SP_IDENTIFIER) IdType id,
			@RequiredParam(name = MedicationRequest.SP_SUBJECT) String patientIdentificationNumber) {
		String ide = id.getIdPart();
		try {
			String[] parts = ide.split("-",2);
			assertFormatPrescriptionId(parts);
			assertDomainNumber(parts[0]);
			Bundle response = bundleResource.assembleMedicationRequest(parts[1], patientIdentificationNumber);
			return response;
		} catch (PrescriptionException ex) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue()
					.setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setCode(OperationOutcome.IssueType.INVALID)
					.setDiagnostics(ex.getMessage());
			throw new InvalidRequestException("Get failed", outcome);
		}
	}

	private void assertDomainNumber(String part) {
		if(Integer.parseInt(cleanDomainId(part)) != domainNumber) {
			throw new PrescriptionException(PrescriptionExceptionEnum.PRESCRIPTION_DOMAIN_ID_WRONG, HttpStatus.BAD_REQUEST, "El id de dominio de la receta no coincide con el del sistema.");
		}
	}

	private String cleanDomainId(String domainId) {
		return domainId.replaceFirst("^0+","");
	}

	private void assertFormatPrescriptionId(String[] parts) {
		Pattern pattern = Pattern.compile("[0-9a-fA-F-]+");
		if(parts.length != 2 || !pattern.matcher(parts[0]).matches() || !pattern.matcher(parts[1]).matches()) {
			throw new PrescriptionException(PrescriptionExceptionEnum.PRESCRIPTION_ID_WRONG_FORMAT, HttpStatus.BAD_REQUEST, "El id de receta no tiene el formato correcto.");
		}
	}
}
