package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PublicAppointmentInstitution {

	@ToString.Include
	private final Integer id;

	@ToString.Include
	private final String cuit;

	@ToString.Include
	private final String sisaCode;

	public PublicAppointmentInstitution(Integer id, String cuit, String sisaCode) {
		this.id = id;
		this.cuit = cuit;
		this.sisaCode = sisaCode;
	}
}