package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;


import lombok.Getter;

@Getter
public class PublicAppointmentPatientDto {

    private final Integer id;

	private final PublicAppointmentPersonDto person;

	public PublicAppointmentPatientDto(Integer id, String firstName, String lastName, String identificationNumber,
									   Short genderId) {
		this.id = id;
		this.person = new PublicAppointmentPersonDto(firstName, lastName, identificationNumber, genderId);
	}
}
