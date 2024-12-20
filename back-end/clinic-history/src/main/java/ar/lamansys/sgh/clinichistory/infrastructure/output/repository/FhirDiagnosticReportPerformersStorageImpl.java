package ar.lamansys.sgh.clinichistory.infrastructure.output.repository;

import ar.lamansys.sgh.clinichistory.application.ports.FhirDiagnosticReportPerformersStorage;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.FhirDiagnosticReportPerformerOrganizationRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.FhirDiagnosticReportPerformerPractitionerRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirDiagnosticReportPerformerOrganization;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.FhirDiagnosticReportPerformerPractitioner;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FhirDiagnosticReportPerformersStorageImpl implements FhirDiagnosticReportPerformersStorage {
	private final FhirDiagnosticReportPerformerPractitionerRepository practitionerRepository;
	private final FhirDiagnosticReportPerformerOrganizationRepository organizationRepository;

	@Override
	public void saveOrganization(Integer diagnosticReportId, String name, String address, String city, String postcode,
		String province, String country, String phoneNumber, String email)
	{
		var org = new FhirDiagnosticReportPerformerOrganization(
			diagnosticReportId,
			name,
			address,
			city,
			postcode,
			province,
			country,
			phoneNumber,
			email
		);
		organizationRepository.save(org);
	}

	@Override
	public void savePractitioner(Integer diagnosticReportId, String identificationNumber, String firstName, String lastName)
	{
		var practitioner = new FhirDiagnosticReportPerformerPractitioner(
				diagnosticReportId,
				identificationNumber, firstName, lastName
		);
		practitionerRepository.save(practitioner);
	}
}
