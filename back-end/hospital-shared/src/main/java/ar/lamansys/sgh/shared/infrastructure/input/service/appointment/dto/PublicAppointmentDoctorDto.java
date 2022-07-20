package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;


import lombok.Getter;

@Getter
public class PublicAppointmentDoctorDto {

	private final String licenseNumber;

	private final PublicAppointmentPersonDto person;

	public PublicAppointmentDoctorDto(String licenseNumber, String firstName, String lastName,
									  String identificationNumber) {
		this.licenseNumber = licenseNumber;
		this.person = new PublicAppointmentPersonDto(firstName, lastName, identificationNumber, null);
	}
}
