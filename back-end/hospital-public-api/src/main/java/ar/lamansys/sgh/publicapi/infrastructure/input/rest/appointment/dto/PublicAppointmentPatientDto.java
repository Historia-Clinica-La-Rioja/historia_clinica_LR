package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment.dto;


import lombok.Getter;

@Getter
public class PublicAppointmentPatientDto {

    private final Integer id;

    private final PublicAppointmentPersonDto person;

	public PublicAppointmentPatientDto(Integer id, PublicAppointmentPersonDto person) {
		this.id = id;
		this.person = person;
	}
}
