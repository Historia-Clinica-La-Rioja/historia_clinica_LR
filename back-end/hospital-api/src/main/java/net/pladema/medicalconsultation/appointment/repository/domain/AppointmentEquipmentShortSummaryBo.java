package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class AppointmentEquipmentShortSummaryBo {

	private String institution;

	private LocalDate date;

	private LocalTime hour;

	private String equipmentName;

}
