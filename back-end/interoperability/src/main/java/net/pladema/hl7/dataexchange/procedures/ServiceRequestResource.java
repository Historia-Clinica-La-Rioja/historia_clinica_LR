package net.pladema.hl7.dataexchange.procedures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

	public List<ServiceRequest> fetchByPatientIdentificationNumber(String patientIdentificationNumber) {
		List<ServiceRequestVo> serviceRequestList = store
			.getServiceRequestByPatientIdentificationNumber(patientIdentificationNumber);
		return buildServiceRequest(removeDuplicates(serviceRequestList));
	}

	public List<ServiceRequest> fetchByIdAndPatientIdentificationNumber(Integer serviceRequestId, String patientIdentificationNumber) {
		List<ServiceRequestVo> serviceRequestList = store
			.getServiceRequestByIdAndPatientIdentificationNumber(serviceRequestId, patientIdentificationNumber);
		return buildServiceRequest(removeDuplicates(serviceRequestList));
	}

	private List<ServiceRequest> buildServiceRequest(List<ServiceRequestVo> serviceRequestList) {

		Map<String, Reference> patientCache = new HashMap<>();
		Map<String, Reference> coverageCache = new HashMap<>();
		Map<String, Reference> practitionerCache = new HashMap<>();
		Map<Integer, Reference> locationCache = new HashMap<>();

		List<ServiceRequest> resources = new ArrayList<>();
		//Skip old service requests that don't have an uuid.
		var serviceRequestsWithUuid = serviceRequestList.stream().filter(s -> s.hasUuid()).collect(Collectors.toList());
		for (ServiceRequestVo serviceRequest : serviceRequestsWithUuid) {
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

			/**
			 * Patient reference
			 * ======================================
			 */
			String patientId = serviceRequest.getPatientId().toString();
			if (!patientCache.containsKey(patientId)){
				Patient resourcePatient = patientResource.fetch(patientId, new EnumMap<>(ResourceType.class));
				Reference patientReference = newReference(fullDomainUrl(resourcePatient));
				patientReference.setDisplay(resourcePatient.getNameFirstRep().getText());
				patientReference.setResource(resourcePatient);
				resource.setSubject(patientReference);
				patientCache.put(patientId, patientReference);
			} else {
				resource.setSubject(patientCache.get(patientId));
			}

			/**
			 * Coverage reference
			 * ======================================
			 */
			if (serviceRequest.getMedicalCoverageId() != null) {
				String medicalCoverageId = serviceRequest.getMedicalCoverageId().toString();
				if (!coverageCache.containsKey(medicalCoverageId)) {
					//Adapt the reference cache to the coverageResource.fetch interface
					Coverage resourceCoverage = coverageResource.fetch(
						medicalCoverageId,
						adaptCacheForCoverage(patientId, patientCache)
					);
					Reference coverageReference = newReference(fullDomainUrl(resourceCoverage));
					coverageReference.setDisplay(resourceCoverage.getPayorFirstRep().getDisplay());
					coverageReference.setResource(resourceCoverage);
					resource.setInsurance(List.of(coverageReference));
					coverageCache.put(medicalCoverageId, coverageReference);
				} else {
					resource.setInsurance(List.of(coverageCache.get(medicalCoverageId)));
				}
			}

			/**
			 * Practitioner reference
			 * ======================================
			 */
			String doctorId = serviceRequest.getDoctorId().toString();
			if (!practitionerCache.containsKey(doctorId)) {
				Practitioner resourcePractitioner = practitionerResource.fetch(doctorId, new EnumMap<>(ResourceType.class));
				Reference practitionerReference = newReference(fullDomainUrl(resourcePractitioner));
				practitionerReference.setDisplay(resourcePractitioner.getNameFirstRep().getText());
				practitionerReference.setResource(resourcePractitioner);
				resource.setRequester(practitionerReference);
				practitionerCache.put(doctorId, practitionerReference);
			} else {
				resource.setRequester(practitionerCache.get(doctorId));
			}

			/**
			 * Location reference
			 * ======================================
			 */
			Integer institutionId = serviceRequest.getInstitutionId();
			if (!locationCache.containsKey(institutionId)) {
				Location resourceLocation = locationResource.fetchByOrganization(store.getOrganizationFromId(institutionId));
				Reference locationReference = newReference(fullDomainUrl(resourceLocation));
				locationReference.setDisplay(resourceLocation.getName());
				locationReference.setResource(resourceLocation);
				resource.setLocationReference(List.of(locationReference));
				locationCache.put(institutionId, locationReference);
			} else {
				resource.setLocationReference(List.of(locationCache.get(institutionId)));
			}

			resource.addNote(new Annotation(new MarkdownType(serviceRequest.getDescription())));

			resources.add(resource);
		}
		return resources;
	}

	/**
	 * The query returns a row for each diagnostic report.
	 * When a service request is completed, its diagnostic reports are duplicated
	 * (for each one with status=initialized there's one with a finished status).
	 * This method returns the latest version of each diagnostic report
	 */
	private List<ServiceRequestVo> removeDuplicates(List<ServiceRequestVo> serviceRequestList) {
		List<ServiceRequestVo> result = new ArrayList<>();
		serviceRequestList.forEach(row -> {
			var hasChild = serviceRequestList
				.stream()
				.anyMatch(child -> Objects.equals(row.getDiagnosticReportId(), child.getDiagnosticReportParentId()));
			if (!hasChild)
				result.add(row);
		});
		return result;
	}

	private Map<ResourceType, Reference> adaptCacheForCoverage(String patientId, Map<String, Reference> referencesCache) {
		Map<ResourceType, Reference> ret = new HashMap<>();
		ret.put(ResourceType.Patient, referencesCache.get(patientId));
		return ret;
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
