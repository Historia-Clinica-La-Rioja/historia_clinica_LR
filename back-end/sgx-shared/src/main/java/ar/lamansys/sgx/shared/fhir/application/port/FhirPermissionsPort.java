package ar.lamansys.sgx.shared.fhir.application.port;

public interface FhirPermissionsPort {
	boolean canPostMedicationRequest();
	boolean canPostDiagnosticReport();
    boolean shouldEnableFhirResources();
	boolean canFetchServiceRequest();
	boolean canFetchMedicationRequest();
	boolean canFetchDocumentReference();
	boolean canFetchBundle();
}
