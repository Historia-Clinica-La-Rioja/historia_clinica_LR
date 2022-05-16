package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import lombok.Getter;

@Getter
public class PublicAppointmentMedicalCoverage {

	private final String cuit;

	private final String name;

	private final String affiliateNumber;

	public PublicAppointmentMedicalCoverage(String cuit, String name,
											String affiliateNumber) {
		this.cuit = cuit;
		this.name = name;
		this.affiliateNumber = affiliateNumber;
	}
}
