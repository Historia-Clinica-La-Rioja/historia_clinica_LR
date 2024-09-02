package net.pladema.medicalconsultation.appointment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDiaryAppointmentBo {

	private Integer id;

	private LocalDate date;

	private LocalTime time;

	private Short stateId;

	private Short medicalAttentionTypeId;

}
