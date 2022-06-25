package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PublicAppointmentPatientDto {

    private final Integer id;

	private final PublicAppointmentPersonDto person;

}
