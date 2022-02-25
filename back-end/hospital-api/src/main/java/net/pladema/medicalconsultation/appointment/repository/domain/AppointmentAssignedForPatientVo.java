package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Getter
@AllArgsConstructor
public class AppointmentAssignedForPatientVo {

	private final String license;

	private final Integer personId;

	private final LocalDate date;

	private final LocalTime hour;

	private final String office;

}
