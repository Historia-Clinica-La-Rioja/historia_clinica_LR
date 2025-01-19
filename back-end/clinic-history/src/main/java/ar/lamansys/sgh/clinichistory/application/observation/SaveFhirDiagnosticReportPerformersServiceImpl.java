package ar.lamansys.sgh.clinichistory.application.observation;

import ar.lamansys.sgh.clinichistory.application.ports.FhirDiagnosticReportPerformersStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirDiagnosticReportPerformersDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.observation.SharedFhirDiagnosticReportPerformersPort;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveFhirDiagnosticReportPerformersServiceImpl implements SharedFhirDiagnosticReportPerformersPort {

	private final FhirDiagnosticReportPerformersStorage fhirDiagnosticReportPerformersStorage;

	@Override
	public void savePerformers(Integer diagnosticReportId, FhirDiagnosticReportPerformersDto performersDto) {
		performersDto
		.getOrganizations()
		.forEach(organization ->
			fhirDiagnosticReportPerformersStorage.saveOrganization(
				diagnosticReportId,
				organization.getName(),
				organization.getAddress(),
				organization.getCity(),
				organization.getPostcode(),
				organization.getProvince(),
				organization.getCountry(),
				organization.getPhoneNumber(),
				organization.getEmail()
			)
		);

		performersDto
		.getPractitioners()
		.forEach(practitioner ->
			fhirDiagnosticReportPerformersStorage.savePractitioner(
				diagnosticReportId,
				practitioner.getIdentificationNumber(),
				practitioner.getFirstName(),
				practitioner.getLastName()
			)
		);
	}
}
