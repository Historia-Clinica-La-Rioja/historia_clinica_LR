package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ToString
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AppointmentSearchBo {

	private List<Short> daysOfWeek;
	private String aliasOrSpecialtyName;
	private LocalTime initialSearchTime;
	private LocalTime endSearchTime;
	private LocalDate initialSearchDate;
	private LocalDate endingSearchDate;

}
