package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class InstitutionBo {

	@ToString.Include
	private final Integer id;

	@ToString.Include
	private final String cuit;

	@ToString.Include
	private final String sisaCode;

	public InstitutionBo(Integer id, String cuit, String sisaCode) {
		this.id = id;
		this.cuit = cuit;
		this.sisaCode = sisaCode;
	}
}
