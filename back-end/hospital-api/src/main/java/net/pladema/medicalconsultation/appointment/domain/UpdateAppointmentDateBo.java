package net.pladema.medicalconsultation.appointment.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAppointmentDateBo {

	private Integer appointmentId;

	private LocalTime time;

	private LocalDate date;

	private Integer openingHoursId;

	private Short modalityId;

	private String patientEmail;

}
