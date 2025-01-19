package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hl7.dataexchange.exceptions.ServiceRequestException;
import net.pladema.hl7.dataexchange.exceptions.ServiceRequestExceptionEnum;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;

import net.pladema.hl7.supporting.exchange.documents.BundleResource;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.ServiceRequest;
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
public class ServiceRequestProvider implements IResourceProvider {

	private final BundleResource bundleResource;

	@Value("${prescription.domain.number}")
	private int domainNumber;

	public ServiceRequestProvider(BundleResource bundleResource) {
		this.bundleResource = bundleResource;
	}

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return ServiceRequest.class;
	}

	@GetMapping(value = "/ServiceRequest", params = {ServiceRequest.SP_IDENTIFIER, ServiceRequest.SP_SUBJECT})
	@Search
	public Bundle getServiceRequestByIdAndIdentificationNumber(
			@RequiredParam(name = ServiceRequest.SP_IDENTIFIER) IdType serviceRequestIdAndDomainNumber,
			@RequiredParam(name = ServiceRequest.SP_SUBJECT) String patientIdentificationNumber) {
		log.debug("Input parameters -> id {}, patientIdentificationNumber {}",serviceRequestIdAndDomainNumber,patientIdentificationNumber);
		try {
			String ide = serviceRequestIdAndDomainNumber.getIdPart();
			String[] parts = ide.split("-");
			assertFormatOrderId(parts);
			assertDomainNumber(parts[0]);
			Integer serviceRequestId = Integer.parseInt(parts[1]);
			Bundle response = bundleResource.buildBundleGetServiceRequestByIdAndPatientIdentification(serviceRequestId, patientIdentificationNumber);
			log.debug("Bundle -> {}", response);
			return response;
		} catch (ServiceRequestException|NumberFormatException ex) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue()
					.setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setCode(OperationOutcome.IssueType.INVALID)
					.setDiagnostics(ex.getMessage());
			throw new InvalidRequestException("Get failed", outcome);
		}
	}

	/**
	 * Returns all service requests assigned to the given patient
	 */
	@GetMapping(value = "/ServiceRequest", params = {ServiceRequest.SP_SUBJECT})
	@Search
	public Bundle getServiceRequestByIdentificationNumber(
			@RequiredParam(name = ServiceRequest.SP_SUBJECT) String patientIdentificationNumber) {
		log.debug("Input parameters -> patientIdentificationNumber {}", patientIdentificationNumber);
		try {
			Bundle response = bundleResource.buildBundleGetServiceRequestsByPatientIdentification(patientIdentificationNumber);
			log.debug("Bundle -> {}", response);
			return response;
		} catch (ServiceRequestException ex) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue()
					.setSeverity(OperationOutcome.IssueSeverity.ERROR)
					.setCode(OperationOutcome.IssueType.INVALID)
					.setDiagnostics(ex.getMessage());
			throw new InvalidRequestException("Get failed", outcome);
		}
	}

	private void assertDomainNumber(String part) {
		if(Integer.parseInt(part) != domainNumber) {
			throw new ServiceRequestException(ServiceRequestExceptionEnum.SERVICE_REQUEST_DOMAIN_ID_WRONG, HttpStatus.BAD_REQUEST, "El id de dominio de la orden no coincide con el del sistema");
		}
	}

	private void assertFormatOrderId(String[] parts) {
		Pattern pattern = Pattern.compile("\\d+");
		if(parts.length != 2 || !pattern.matcher(parts[0]).matches() || !pattern.matcher(parts[1]).matches()) {
			throw new ServiceRequestException(ServiceRequestExceptionEnum.SERVICE_REQUEST_ID_WRONG_FORMAT, HttpStatus.BAD_REQUEST, "El id de orden no tiene el formato correcto.");
		}
	}

}
