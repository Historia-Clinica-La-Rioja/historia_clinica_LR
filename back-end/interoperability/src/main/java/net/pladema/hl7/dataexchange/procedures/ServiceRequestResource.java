package net.pladema.hl7.dataexchange.procedures;

import net.pladema.hl7.concept.administration.CoverageResource;
import net.pladema.hl7.concept.administration.LocationResource;
import net.pladema.hl7.concept.administration.PatientResource;
import net.pladema.hl7.concept.administration.PractitionerResource;
import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.domain.ServiceRequestVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;

import net.pladema.hl7.supporting.terminology.coding.CodingValueSet;

import org.hl7.fhir.r4.model.Annotation;
import org.hl7.fhir.r4.model.BaseResource;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MarkdownType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Conditional(InteroperabilityCondition.class)
public class ServiceRequestResource extends IResourceFhir {

	private final LocationResource locationResource;
	private final PractitionerResource practitionerResource;
	private final CoverageResource coverageResource;
	private final PatientResource patientResource;
	@Value("${prescription.domain.number}")
	private int domainNumber;


	protected ServiceRequestResource(FhirPersistentStore store, LocationResource locationResource, PractitionerResource practitionerResource, CoverageResource coverageResource, PatientResource patientResource) {
		super(store);
		this.locationResource = locationResource;
		this.practitionerResource = practitionerResource;
		this.coverageResource = coverageResource;
		this.patientResource = patientResource;
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.ServiceRequest;
	}

	public List<ServiceRequest> fetch(Integer id, String identificationNumber) {
		List<ServiceRequestVo> serviceRequestList = store.getServiceRequest(id,identificationNumber);

		Map<ResourceType, Reference> references = new HashMap<>();

		List<ServiceRequest> resources = new ArrayList<>();

		for (ServiceRequestVo serviceRequest : serviceRequestList) {
			ServiceRequest resource = new ServiceRequest();

			resource.setMeta(new Meta().setProfile(List.of(new CanonicalType(CodingProfile.ServiceRequest.BASEURL))));

			//resource.setId(domainNumber + "-" + serviceRequest.getSrId().toString().concat("-").concat(serviceRequest.getDiagnosticReportId().toString()));
			resource.setId(serviceRequest.getDiagnosticReportUuid().toString());
			resource.addIdentifier(newIdentifier(resource));
			resource.setRequisition(newIdentifier(resource,domainNumber + "-" + serviceRequest.getServiceRequestUuid().toString()));
			
			resource.setStatus(getStatusFromServiceRequest(serviceRequest.getServiceRequestStatus()));
			resource.setIntent(getIntentFromServiceRequest(serviceRequest.getIntentId()));
			resource.setCategory(Collections.singletonList(getCategory(serviceRequest.getCategoryId())));
			resource.setPriority(ServiceRequest.ServiceRequestPriority.ROUTINE); //siempre sera routine ??
			resource.setAuthoredOn(serviceRequest.getRequestDate());
			resource.setCode(newCodeableConcept(CodingSystem.SNOMED,new FhirCode(serviceRequest.getSnomedId(),serviceRequest.getSnomedPt())));
			resource.setReasonCode(Collections.singletonList(newCodeableConcept(CodingSystem.SNOMED, new FhirCode(serviceRequest.getProblemId(),serviceRequest.getProblemPt()))));

			if (!references.containsKey(ResourceType.Patient)) {
				Patient resourcePatient = patientResource.fetch(serviceRequest.getPatientId().toString(), new EnumMap<>(ResourceType.class));
				Reference patientReference = newReference(fullDomainUrl(resourcePatient));
				patientReference.setDisplay(resourcePatient.getNameFirstRep().getText());
				patientReference.setResource(resourcePatient);
				resource.setSubject(patientReference);
				references.put(ResourceType.Patient, patientReference);
			} else {
				resource.setSubject(references.get(ResourceType.Patient));
			}

			if (!references.containsKey(ResourceType.Coverage)) {
				if (serviceRequest.getMedicalCoverageId() != null) {
					Coverage resourceCoverage = coverageResource.fetch(serviceRequest.getMedicalCoverageId().toString(), references);
					Reference coverageReference = newReference(fullDomainUrl(resourceCoverage));
					coverageReference.setDisplay(resourceCoverage.getPayorFirstRep().getDisplay());
					coverageReference.setResource(resourceCoverage);
					resource.setInsurance(List.of(coverageReference));
					references.put(ResourceType.Coverage, coverageReference);
				}
			} else {
				resource.setInsurance(List.of(references.get(ResourceType.Coverage)));
			}

			if (!references.containsKey(ResourceType.Practitioner)) {
				Practitioner resourcePractitioner = practitionerResource.fetch(serviceRequest.getDoctorId().toString(), new EnumMap<>(ResourceType.class));
				Reference practitionerReference = newReference(fullDomainUrl(resourcePractitioner));
				practitionerReference.setDisplay(resourcePractitioner.getNameFirstRep().getText());
				practitionerReference.setResource(resourcePractitioner);
				resource.setRequester(practitionerReference);
				references.put(ResourceType.Practitioner, practitionerReference);
			} else {
				resource.setRequester(references.get(ResourceType.Practitioner));
			}

			if (!references.containsKey(ResourceType.Location)) {
				Location resourceLocation = locationResource.fetchByOrganization(store.getOrganizationFromId(serviceRequest.getInstitutionId()));
				Reference locationReference = newReference(fullDomainUrl(resourceLocation));
				locationReference.setDisplay(resourceLocation.getName());
				locationReference.setResource(resourceLocation);
				resource.setLocationReference(List.of(locationReference));
				references.put(ResourceType.Location, locationReference);
			} else {
				resource.setLocationReference(List.of(references.get(ResourceType.Location)));
			}

			resource.addNote(new Annotation(new MarkdownType(serviceRequest.getDescription())));

			resources.add(resource);
		}
		return resources;
	}

	private ServiceRequest.ServiceRequestStatus getStatusFromServiceRequest(String id) {
		switch(id) {
			case ("55561003"):
				return ServiceRequest.ServiceRequestStatus.ACTIVE;
			case ("281341005"):
				return ServiceRequest.ServiceRequestStatus.ONHOLD;
			case ("255594003"):
				return ServiceRequest.ServiceRequestStatus.COMPLETED;
			case ("723510000"):
				return ServiceRequest.ServiceRequestStatus.ENTEREDINERROR;
			case ("255609007"):
				return ServiceRequest.ServiceRequestStatus.DRAFT;
			case ("261665006"):
				return ServiceRequest.ServiceRequestStatus.UNKNOWN;
			default:
				return ServiceRequest.ServiceRequestStatus.NULL;
		}
	}

	private ServiceRequest.ServiceRequestIntent getIntentFromServiceRequest(String id) {
		switch(id) {
			case ("1"):
				return ServiceRequest.ServiceRequestIntent.PROPOSAL;
			case ("2"):
				return ServiceRequest.ServiceRequestIntent.PLAN;
			case ("3"):
				return ServiceRequest.ServiceRequestIntent.ORDER;
			case ("4"):
				return ServiceRequest.ServiceRequestIntent.ORIGINALORDER;
			case ("5"):
				return ServiceRequest.ServiceRequestIntent.REFLEXORDER;
			case ("6"):
				return ServiceRequest.ServiceRequestIntent.FILLERORDER;
			case ("7"):
				return ServiceRequest.ServiceRequestIntent.INSTANCEORDER;
			case ("8"):
				return ServiceRequest.ServiceRequestIntent.OPTION;
			default:
				return ServiceRequest.ServiceRequestIntent.NULL;
		}
	}

	private CodeableConcept getCategory(String code) {
		switch(code) {
			case ("108252007"):
				return CodingValueSet.ServiceRequest.CATEGORY.LABORATORY_PROCEDURE.getCodeableConcept();
			case ("363679005"):
				return CodingValueSet.ServiceRequest.CATEGORY.IMAGING.getCodeableConcept();
			case ("409063005"):
				return CodingValueSet.ServiceRequest.CATEGORY.COUNSELLING.getCodeableConcept();
			case ("409073007"):
				return CodingValueSet.ServiceRequest.CATEGORY.EDUCATION.getCodeableConcept();
			case ("387713003"):
				return CodingValueSet.ServiceRequest.CATEGORY.SURGICAL_PROCEDURE.getCodeableConcept();
			default:
				return null;
		}
	}

	public void updateStatus(Integer serviceRequestId) {
		if (store.isServiceRequestCompleted(serviceRequestId))
			store.setServiceRequestCompleted(serviceRequestId);
	}

	public static ServiceRequestVo encode(BaseResource baseResource) {
		ServiceRequestVo data = new ServiceRequestVo();
		ServiceRequest resource = (ServiceRequest) baseResource;

		data.setDiagnosticReportUuid(UUID.fromString(resource.getIdElement().getIdPart()));
		data.setServiceRequestUuid(UUID.fromString(resource.getRequisition().getValue().split("-",2)[1]));

		data.setServiceRequestStatus(resource.getStatus().getDisplay());
		data.setIntentId(resource.getIntent().getDisplay());
		data.setCategoryId(resource.getCategoryFirstRep().getText());

		Coding code = resource.getCode().getCodingFirstRep();
		data.setSnomedId(code.getCode());
		data.setSnomedPt(code.getDisplay());

		Coding reasonCode = resource.getReasonCodeFirstRep().getCodingFirstRep();
		data.setProblemId(reasonCode.getCode());
		data.setProblemPt(reasonCode.getDisplay());

		data.setRequestDate(resource.getAuthoredOn());
		return data;
	}
}
