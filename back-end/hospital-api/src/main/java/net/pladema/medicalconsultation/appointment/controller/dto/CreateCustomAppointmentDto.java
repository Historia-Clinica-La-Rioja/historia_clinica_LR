package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomAppointmentDto {

	private CreateAppointmentDto createAppointmentDto;

	private CustomRecurringAppointmentDto customRecurringAppointmentDto;
}
