package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment.dto;

import lombok.Getter;

@Getter
public class PublicAppointmentMedicalCoverage {

	private final Integer healthInsuranceId;

	private final String medicalCoverageName;

	private final String medicalCoverageAffiliateNumber;

	public PublicAppointmentMedicalCoverage(Integer healthInsuranceId,
											String medicalCoverageName,
											String medicalCoverageAffiliateNumber) {
		this.healthInsuranceId = healthInsuranceId;
		this.medicalCoverageName = medicalCoverageName;
		this.medicalCoverageAffiliateNumber = medicalCoverageAffiliateNumber;
	}
}
