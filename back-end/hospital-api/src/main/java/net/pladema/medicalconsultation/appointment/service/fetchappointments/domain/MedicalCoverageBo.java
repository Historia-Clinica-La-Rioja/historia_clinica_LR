package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class MedicalCoverageBo {

	@ToString.Include
	private final String name;

	@ToString.Include
	private final String cuit;

	@ToString.Include
	private final String affiliateNumber;

	public MedicalCoverageBo(String name, String cuit, String affiliateNumber) {
		this.name = name;
		this.cuit = cuit;
		this.affiliateNumber = affiliateNumber;
	}
}
