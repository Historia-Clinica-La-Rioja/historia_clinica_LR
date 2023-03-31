package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PublicAppointmentClinicalSpecialty {

	@ToString.Include
	private final String sctid;

	@ToString.Include
	private final Integer id;

	@ToString.Include
	private final String name;

	public PublicAppointmentClinicalSpecialty(String sctid, Integer id, String name) {
		this.sctid = sctid;
		this.id = id;
		this.name = name;
	}
}