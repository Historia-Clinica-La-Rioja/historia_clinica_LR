package ar.lamansys.sgh.clinichistory.application.ports;

public interface FhirDiagnosticReportPerformersStorage {
	void saveOrganization(
		Integer diagnosticReportId,
		String name,
		String address,
		String city,
		String postcode,
		String province,
		String country,
		String phoneNumber,
		String email
	);

	void savePractitioner(Integer diagnosticReportId, String identificationNumber, String firstName, String lastName);
}
