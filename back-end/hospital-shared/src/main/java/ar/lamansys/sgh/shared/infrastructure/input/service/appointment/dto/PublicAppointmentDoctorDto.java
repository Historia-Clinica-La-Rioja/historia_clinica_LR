package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PublicAppointmentDoctorDto {

	private final String licenseNumber;

	private final PublicAppointmentPersonDto person;

}
