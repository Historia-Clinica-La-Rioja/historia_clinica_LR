//
//FHIR Documents.
//FHIR resources can be used to build documents that represent a composition: a coherent set
//of information that is a statement of healthcare information. A document is an immutable
//set of resources with a fixed presentation that is authored and/or attested by humans,
//organizations and devices.
//FHIR documents are for documents that are authored and assembled in FHIR, while the
// document reference resource is for general references to pre-existing documents.
//

package net.pladema.hl7.supporting.exchange.documents;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ca.uhn.fhir.rest.param.TokenParam;

import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import net.pladema.hl7.concept.administration.CoverageResource;
import net.pladema.hl7.concept.administration.LocationResource;
import net.pladema.hl7.concept.administration.OrganizationResource;
import net.pladema.hl7.concept.administration.PatientResource;
import net.pladema.hl7.concept.administration.PractitionerResource;
import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.clinical.AllergyIntoleranceResource;
import net.pladema.hl7.dataexchange.clinical.ConditionResource;
import net.pladema.hl7.dataexchange.exceptions.PrescriptionException;
import net.pladema.hl7.dataexchange.exceptions.PrescriptionExceptionEnum;
import net.pladema.hl7.dataexchange.exceptions.DispenseValidationException;
import net.pladema.hl7.dataexchange.exceptions.ServiceRequestException;
import net.pladema.hl7.dataexchange.exceptions.ServiceRequestExceptionEnum;
import net.pladema.hl7.dataexchange.medications.ImmunizationResource;
import net.pladema.hl7.dataexchange.medications.MedicationDispenseResource;
import net.pladema.hl7.dataexchange.medications.MedicationRequestResource;
import net.pladema.hl7.dataexchange.medications.MedicationStatementResource;
import net.pladema.hl7.dataexchange.model.adaptor.FhirID;
import net.pladema.hl7.dataexchange.model.domain.BundleVo;
import net.pladema.hl7.dataexchange.model.domain.DiagnosticReportVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationDispenseVo;
import net.pladema.hl7.dataexchange.procedures.DiagnosticReportResource;
import net.pladema.hl7.dataexchange.procedures.ObservationResource;
import net.pladema.hl7.dataexchange.procedures.ServiceRequestResource;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.dataexchange.model.domain.PatientSummaryVo;
import net.pladema.hl7.supporting.exchange.documents.profile.FhirDocument;
import net.pladema.hl7.supporting.exchange.documents.ips.PatientSummaryDocument;
import net.pladema.hl7.supporting.exchange.restful.validator.DocumentReferenceValidation;
import ar.lamansys.sgx.shared.fhir.application.port.FhirPermissionsPort;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;

import org.hl7.fhir.r4.model.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Conditional(InteroperabilityCondition.class)
public class BundleResource extends IResourceFhir {

    private final DocumentReferenceResource documentReferenceResource;
    private final DocumentReferenceValidation documentReferenceValidation;
    private final FhirDocument fhirDocument;

	private final MedicationRequestResource medicationRequestResource;
	private final MedicationDispenseResource medicationDispenseResource;
	private final ServiceRequestResource serviceRequestResource;
	private final DiagnosticReportResource diagnosticReportResource;
	private final FeatureFlagsService featureFlagsService;
	private final FhirPermissionsPort fhirPermissionsPort;

    @Value("${app.default.language}")
    private String language;

    @Autowired
    public BundleResource(FhirPersistentStore store,
						  DocumentReferenceResource documentReferenceResource,
						  DocumentReferenceValidation documentReferenceValidation,
						  FhirDocument fhirDocument, MedicationRequestResource medicationRequestResource, MedicationDispenseResource medicationDispenseResource,
						  ServiceRequestResource serviceRequestResource, DiagnosticReportResource diagnosticReportResource, FeatureFlagsService featureFlagsService,
						  FhirPermissionsPort fhirPermissionsPort) {
        super(store);
        this.documentReferenceResource = documentReferenceResource;
        this.documentReferenceValidation=documentReferenceValidation;
        this.fhirDocument=fhirDocument;
		this.medicationRequestResource = medicationRequestResource;
		this.medicationDispenseResource = medicationDispenseResource;
		this.serviceRequestResource = serviceRequestResource;
		this.diagnosticReportResource = diagnosticReportResource;
		this.featureFlagsService = featureFlagsService;
		this.fhirPermissionsPort = fhirPermissionsPort;
	}

    @Override
    public ResourceType getResourceType() {
        return ResourceType.Bundle;
    }

    public Bundle getExistingDocumentsReferences (TokenParam subject,
                                                  TokenParam custodian, TokenParam type) {
		assertCanFetchDocumentReference();
        //Input parameters required validation
        return documentReferenceValidation.inputParameter(subject, custodian, type, getDominio()).orElseGet(
                //returns a default value directly if the Optional is empty (the data of document is valid)
                () -> {
                    BundleVo data = documentReferenceResource.getData(subject.getValue());
                    //Data required validation
                    return documentReferenceValidation.data(data, subject.getValue()).orElseGet(
                            () -> { Bundle validDocument = empty();
                                validDocument.addEntry(documentReferenceResource.fetchEntry(data));
                                validDocument.setTotal(validDocument.getEntry().size());
                                return validDocument;
                            });
                }
        );
    }

    public Bundle assembleDocument(IdType id) {
    	assertCanFetchBundle();
        //TODO should be replaced by database real search
        Coding code = PatientSummaryDocument.TYPE;

        Bundle resource = new Bundle();
        resource.setId(FhirID.autoGenerated());
        resource.setIdentifier(newIdentifier(resource));
        resource.setMeta(new Meta().setLastUpdated(new Date()));
        resource.setType(Bundle.BundleType.DOCUMENT);
        resource.setTimestamp(new Date());
        resource.setLanguage(language);

        //=======================Content=======================
        fhirDocument.get(code).ifPresent(document ->
                resource.setEntry(document.getContent(id.getIdPart()))
        );
        return resource;
    }

    public static Bundle empty(){
        Bundle document = new Bundle();
        document.setId(FhirID.autoGenerated());
        document.setType(Bundle.BundleType.SEARCHSET);
        document.setMeta(new Meta().setLastUpdated(new Date()));
        document.setTotal(0);
        return document;
    }

    public PatientSummaryVo encodeResourceToSummary(Bundle resource) {
        PatientSummaryVo summary = new PatientSummaryVo();
        List<Bundle.BundleEntryComponent> entries = resource.getEntry();
        entries.forEach(entry -> {
            switch(entry.getResource().getResourceType()){
                case Patient:
                    summary.setPatient(PatientResource.encode(entry.getResource()));
                    break;
                case Condition:
                    summary.addCondition(ConditionResource.encode(entry.getResource()));
                    break;
                case MedicationStatement:
                    Optional<Resource> medication = getMedication(entry.getResource(), entries);
                    summary.addMedication(MedicationStatementResource.encode(entry.getResource(), medication));
                    break;
                case Immunization:
                    summary.addImmunization(ImmunizationResource.encode(entry.getResource()));
                    break;
                case AllergyIntolerance:
                    summary.addAllergy(AllergyIntoleranceResource.encode(entry.getResource()));
                    break;
                case Organization:
                    summary.setOrganization(OrganizationResource.encode(entry.getResource()));
                    break;
                default:
            }
        });
        return summary;
    }

	public Bundle assembleMedicationRequest(String id, String identificationNumber) {
		assertCanFetchMedicationRequest();
		Bundle resource = new Bundle();
		resource.setId(id);
		/*resource.setIdentifier(newIdentifier(resource));
		resource.setMeta(new Meta().setLastUpdated(new Date()));*/
		resource.setType(Bundle.BundleType.TRANSACTION);
		//resource.setTimestamp(new Date());
		resource.setLanguage(language);

		List<Bundle.BundleEntryComponent> entries = new ArrayList<>();

		List<MedicationRequest> medicationRequest = medicationRequestResource.fetch(id,identificationNumber);

		if (!medicationRequest.isEmpty()) {
			medicationRequest.forEach(mr -> entries.add(createBundleEntry(mr, "PUT", "MedicationRequest")));
			entries.add(createBundleEntry((Resource) medicationRequest.get(0).getSubject().getResource(), "PUT", "Patient"));
			entries.add(createBundleEntry((Resource) medicationRequest.get(0).getRequester().getResource(), "PUT", "Practitioner"));
			if (medicationRequest.get(0).getInsuranceFirstRep().getResource() != null)
				entries.add(createBundleEntry((Resource) medicationRequest.get(0).getInsuranceFirstRep().getResource(), "PUT", "Coverage"));
			//entries.add(createBundleEntry((Resource) medicationRequest.get(0).getPerformer().getResource(), "PUT", "Organization"));
			entries.add(createBundleEntry((Resource) medicationRequest.get(0).getSupportingInformationFirstRep().getResource(), "PUT", "Location"));
		} else {
			throw new PrescriptionException(PrescriptionExceptionEnum.PRESCRIPTION_NOT_FOUND, HttpStatus.NOT_FOUND, "Prescripcion no encontrada con los datos concedidos.");
		}

		resource.setEntry(entries);

		return resource;
	}

	public void processBundle(Bundle bundle) {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_API_FHIR_DISPENSA_Y_CARGA_RESULTADOS_LABORATORIO))
			throw new NotImplementedOperationException("Operation not implemented");

		Map<ResourceType, Resource> resources = new HashMap<>();
		List<Bundle.BundleEntryComponent> entries = bundle.getEntry();
		entries.forEach(e -> resources.put(e.getResource().getResourceType(), e.getResource()));
		if (resources.containsKey(ResourceType.MedicationDispense)) {
			assertCanPostMedicationRequest();
			try {
				MedicationDispenseVo medicationDispenseVo = encodeDispenseBundle(resources);
				medicationDispenseResource.validateDispense(medicationDispenseVo);
				medicationDispenseResource.uppdateRequestDispensed(Integer.parseInt(medicationDispenseVo.getMedicationId()), medicationDispenseVo.getStatusId());
				//medicationRequestResource.updateStatusCompleted(Integer.parseInt(medicationDispenseVo.getMedicationRequestId()));
			} catch (DispenseValidationException ex) {
				OperationOutcome outcome = new OperationOutcome();
				outcome.addIssue()
						.setSeverity(OperationOutcome.IssueSeverity.ERROR)
						.setCode(OperationOutcome.IssueType.INVALID)
						.setDiagnostics(ex.getMessage());
				throw new InvalidRequestException("Validation failed", outcome);
			}
		}
		if (resources.containsKey(ResourceType.DiagnosticReport)) {
			assertCanPostDiagnosticReport();
			try {
				DiagnosticReportVo diagnosticReportVo = encodeDiagnosticReportBundle(resources);
				entries.forEach(e -> {
					if (e.getResource().getResourceType().equals(ResourceType.Observation))
						diagnosticReportVo.addObservation(ObservationResource.encode(e.getResource()));
				});
				diagnosticReportResource.validateDiagnosticReport(diagnosticReportVo);
				diagnosticReportResource.saveDiagnosticReport(diagnosticReportVo);
				serviceRequestResource.updateStatus(Integer.parseInt(diagnosticReportVo.getServiceRequestId()));
			} catch (DispenseValidationException ex) {
				OperationOutcome outcome = new OperationOutcome();
				outcome.addIssue()
						.setSeverity(OperationOutcome.IssueSeverity.ERROR)
						.setCode(OperationOutcome.IssueType.INVALID)
						.setDiagnostics(ex.getMessage());
				throw new InvalidRequestException("Validation failed", outcome);
			}
		}

	}

	private void assertCanPostMedicationRequest() {
		if (!fhirPermissionsPort.canPostMedicationRequest())
			throw new AuthenticationException("Post MedicationRequest permissions missing");
	}

	private void assertCanPostDiagnosticReport() {
		if (!fhirPermissionsPort.canPostDiagnosticReport())
			throw new AuthenticationException("Post DiagnosticReport permissions missing");
	}

	private void assertCanFetchServiceRequest() {
		if (!fhirPermissionsPort.canFetchServiceRequest())
			throw new AuthenticationException("Fetch ServiceRequest permissions missing");
	}

	private void assertCanFetchMedicationRequest() {
		if (!fhirPermissionsPort.canFetchMedicationRequest())
			throw new AuthenticationException("Fetch MedicationRequest permissions missing");
	}

	private void assertCanFetchDocumentReference() {
		if (!fhirPermissionsPort.canFetchDocumentReference())
			throw new AuthenticationException("Fetch DocumentReference permissions missing");
	}

	private void assertCanFetchBundle() {
		if (!fhirPermissionsPort.canFetchBundle())
			throw new AuthenticationException("Fetch Bundle permissions missing");
	}

	public MedicationDispenseVo encodeDispenseBundle(Map<ResourceType, Resource> resources) {
		/*Map<ResourceType, Resource> resources = new HashMap<>();
		List<Bundle.BundleEntryComponent> entries = bundle.getEntry();
		entries.forEach(e -> resources.put(e.getResource().getResourceType(), e.getResource()));*/
		/*if (!resources.containsKey(ResourceType.MedicationDispense))
			throw new DispenseValidationException(DispenseValidationEnumException.DISPENSE_RESOURCE_NOT_EXIST, HttpStatus.BAD_REQUEST, "Dentro del bundle no existe ningun recurso de dispensa.");*/
		MedicationDispense medicationDispense = (MedicationDispense) resources.get(ResourceType.MedicationDispense);
		if (medicationDispense != null) {
			MedicationDispenseVo medicationDispenseVo = MedicationDispenseResource.encode(medicationDispense);
			if (resources.containsKey(ResourceType.MedicationRequest)) {
				MedicationRequest medicationRequest = (MedicationRequest) resources.get(ResourceType.MedicationRequest);
				medicationDispenseVo.setMedicationRequestVo(MedicationRequestResource.encode(medicationRequest));
			} else
				medicationDispenseVo.setMedicationRequestVo(null);
			if (resources.containsKey(ResourceType.Patient)) {
				Patient patient = (Patient) resources.get(ResourceType.Patient);
				medicationDispenseVo.setPatientVo(PatientResource.encode(patient));
			} else
				medicationDispenseVo.setPatientVo(null);
			if (resources.containsKey(ResourceType.Coverage)) {
				Coverage coverage = (Coverage) resources.get(ResourceType.Coverage);
				medicationDispenseVo.setCoverageVo(CoverageResource.encode(coverage));
			} else
				medicationDispenseVo.setCoverageVo(null);
			if (resources.containsKey(ResourceType.Practitioner)) {
				Practitioner practitioner = (Practitioner) resources.get(ResourceType.Practitioner);
				medicationDispenseVo.setPractitionerVo(PractitionerResource.encode(practitioner));
			} else
				medicationDispenseVo.setPractitionerVo(null);
			if (resources.containsKey(ResourceType.Organization)) {
				Organization organization = (Organization) resources.get(ResourceType.Organization);
				medicationDispenseVo.setOrganizationVo(OrganizationResource.encode(organization));
			} else
				medicationDispenseVo.setOrganizationVo(null);
			if (resources.containsKey(ResourceType.Location)) {
				Location location = (Location) resources.get(ResourceType.Location);
				medicationDispenseVo.setLocationVo(LocationResource.encode(location));
			} else
				medicationDispenseVo.setLocationVo(null);

			return medicationDispenseVo;
		}

		return null;
	}

	private Bundle.BundleEntryComponent createBundleEntry(Resource resource, String method, String url) {
		Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
		entry.setFullUrl(fullDomainUrl(resource));
		entry.setResource(resource);

		Bundle.BundleEntryRequestComponent request = new Bundle.BundleEntryRequestComponent();
		request.setMethod(Bundle.HTTPVerb.fromCode(method));
		request.setUrl(url);
		entry.setRequest(request);

		return entry;
	}

    private Optional<Resource> getMedication(Resource resource,
                                               List<Bundle.BundleEntryComponent> entries) {
        if( ((MedicationStatement) resource).hasMedicationReference()){
            String reference = ((MedicationStatement) resource).getMedicationReference().getReference();
            return entries.stream()
                    .filter(r -> r.getResource().getId().equals(reference))
                    .map(Bundle.BundleEntryComponent::getResource)
                    .findFirst();
        }
        return Optional.empty();
    }

	/**
	 * The bundle contains exactly one entry for the patient, practitioner, coverage and location resources.
	 */
	public Bundle buildBundleGetServiceRequestByIdAndPatientIdentification(Integer serviceRequestId, String patientIdentificationNumber) {
		assertCanFetchServiceRequest();
		List<ServiceRequest> serviceRequest = serviceRequestResource.fetchByIdAndPatientIdentificationNumber(serviceRequestId, patientIdentificationNumber);

		Bundle resource = new Bundle();
		/*resource.setId(FhirID.autoGenerated());
		resource.setIdentifier(newIdentifier(resource));
		resource.setMeta(new Meta().setLastUpdated(new Date()));*/
		resource.setType(Bundle.BundleType.TRANSACTION);
		//resource.setTimestamp(new Date());
		resource.setLanguage(language);

		List<Bundle.BundleEntryComponent> entries = new ArrayList<>();

		if (!serviceRequest.isEmpty()) {
			serviceRequest.forEach(mr -> entries.add(createBundleEntry(mr, "PUT", "ServiceRequest")));
			resource.setId(serviceRequest.get(0).getRequisition().getValue().split("-",2)[1]);
			entries.add(createBundleEntry((Resource) serviceRequest.get(0).getSubject().getResource(), "PUT", "Patient"));
			entries.add(createBundleEntry((Resource) serviceRequest.get(0).getRequester().getResource(), "PUT", "Practitioner"));
			if (serviceRequest.get(0).getInsuranceFirstRep().getResource() != null)
				entries.add(createBundleEntry((Resource) serviceRequest.get(0).getInsuranceFirstRep().getResource(), "PUT", "Coverage"));
			entries.add(createBundleEntry((Resource) serviceRequest.get(0).getLocationReferenceFirstRep().getResource(), "PUT", "Location"));
		} else {
			throw new ServiceRequestException(ServiceRequestExceptionEnum.SERVICE_REQUEST_NOT_FOUND, HttpStatus.NOT_FOUND, "Orden no encontrada con los datos concedidos.");
		}

		resource.setEntry(entries);

		return resource;
	}

	/**
	 * The bundle contains one entry for each service request found. It also has one entry for each of the patients,
	 * practitioners, coverages and locations referenced by the service requests.
	 */
	public Bundle buildBundleGetServiceRequestsByPatientIdentification(String patientIdentificationNumber) {
		assertCanFetchServiceRequest();
		List<ServiceRequest> serviceRequests = serviceRequestResource.fetchByPatientIdentificationNumber(patientIdentificationNumber);
		Bundle resource = new Bundle();
		resource.setType(Bundle.BundleType.TRANSACTION);
		resource.setLanguage(language);
		List<Bundle.BundleEntryComponent> entries = new ArrayList<>();

		serviceRequests.forEach(serviceRequest -> {
			entries.add(createBundleEntry(serviceRequest, "PUT", "ServiceRequest"));
			//resource.setId(serviceRequest.get(0).getRequisition().getValue().split("-",2)[1]);

			if (serviceRequest.getSubject() != null) {
				entries.add(createBundleEntry((Resource) serviceRequest.getSubject().getResource(), "PUT", "Patient"));
			}

			if (serviceRequest.getRequester() != null) {
				entries.add(createBundleEntry((Resource) serviceRequest.getRequester().getResource(), "PUT", "Practitioner"));
			}

			if (serviceRequest.getInsuranceFirstRep().getResource() != null) {
				entries.add(createBundleEntry((Resource) serviceRequest.getInsuranceFirstRep().getResource(), "PUT", "Coverage"));
			}

			if (serviceRequest.getLocationReferenceFirstRep() != null) {
				var a  = createBundleEntry((Resource) serviceRequest.getLocationReferenceFirstRep().getResource(), "PUT", "Location");

				entries.add(a);
			}

		});
		resource.setEntry(deduplicate(entries));
		return resource;
	}

	private List<Bundle.BundleEntryComponent> deduplicate(List<Bundle.BundleEntryComponent> entries) {
		var ret = new ArrayList<Bundle.BundleEntryComponent>();
		entries.forEach(newEntry -> {
			if (!ret.stream().anyMatch(oldEntry -> oldEntry.equalsDeep(newEntry))){
				ret.add(newEntry);
			}
		});
		return ret;
	}


	private DiagnosticReportVo encodeDiagnosticReportBundle(Map<ResourceType, Resource> resources) {
		DiagnosticReport diagnosticReport = (DiagnosticReport) resources.get(ResourceType.DiagnosticReport);
		if (diagnosticReport != null) {
			DiagnosticReportVo diagnosticReportVo = DiagnosticReportResource.encode(diagnosticReport);
			if (resources.containsKey(ResourceType.ServiceRequest)) {
				ServiceRequest serviceRequest = (ServiceRequest) resources.get(ResourceType.ServiceRequest);
				diagnosticReportVo.setServiceRequestVo(ServiceRequestResource.encode(serviceRequest));
			} else
				diagnosticReportVo.setServiceRequestVo(null);
			if (resources.containsKey(ResourceType.Patient)) {
				Patient patient = (Patient) resources.get(ResourceType.Patient);
				diagnosticReportVo.setPatientVo(PatientResource.encode(patient));
			} else
				diagnosticReportVo.setPatientVo(null);
			if (resources.containsKey(ResourceType.Coverage)) {
				Coverage coverage = (Coverage) resources.get(ResourceType.Coverage);
				diagnosticReportVo.setCoverageVo(CoverageResource.encode(coverage));
			} else
				diagnosticReportVo.setCoverageVo(null);
			if (resources.containsKey(ResourceType.Practitioner)) {
				Practitioner practitioner = (Practitioner) resources.get(ResourceType.Practitioner);
				diagnosticReportVo.setPractitionerVo(PractitionerResource.encode(practitioner));
			} else
				diagnosticReportVo.setPractitionerVo(null);
			if (resources.containsKey(ResourceType.Organization)) {
				Organization organization = (Organization) resources.get(ResourceType.Organization);
				diagnosticReportVo.setOrganizationVo(OrganizationResource.encode(organization));
			} else
				diagnosticReportVo.setOrganizationVo(null);
			if (resources.containsKey(ResourceType.Location)) {
				Location location = (Location) resources.get(ResourceType.Location);
				diagnosticReportVo.setLocationVo(LocationResource.encode(location));
			} else
				diagnosticReportVo.setLocationVo(null);

			return diagnosticReportVo;
		}

		return null;
	}


}
