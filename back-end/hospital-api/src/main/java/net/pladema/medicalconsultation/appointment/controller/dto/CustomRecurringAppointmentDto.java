package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CustomRecurringAppointmentDto {

	private LocalDate endDate;
	private Short repeatEvery;
	private Short weekDayId;
}
