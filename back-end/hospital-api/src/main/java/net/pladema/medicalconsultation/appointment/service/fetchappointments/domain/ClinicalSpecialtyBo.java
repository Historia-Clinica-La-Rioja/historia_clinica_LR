package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ClinicalSpecialtyBo {

	@ToString.Include
	private final String sctid;

	@ToString.Include
	private final String name;

	public ClinicalSpecialtyBo(String sctid, String name) {
		this.sctid = sctid;
		this.name = name;
	}
}
