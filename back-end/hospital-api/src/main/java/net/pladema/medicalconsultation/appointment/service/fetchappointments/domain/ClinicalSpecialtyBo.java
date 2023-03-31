package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class ClinicalSpecialtyBo {

	@ToString.Include
	private final String sctid;

	@ToString.Include
	private final Integer id;

	@ToString.Include
	private final String name;

	public ClinicalSpecialtyBo(String sctid, Integer id, String name) {
		this.sctid = sctid;
		this.id = id;
		this.name = name;
	}
}
