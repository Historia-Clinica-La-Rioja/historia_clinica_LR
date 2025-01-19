package net.pladema.medicalconsultation.appointment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UpdateDiaryAppointmentBo {

	private Integer id;

	private LocalDate date;

	private LocalTime time;

	private Short stateId;

	private Short medicalAttentionTypeId;

	private boolean overturn;

	private Integer openingHoursId;

	private Integer patientId;

	private Integer newOpeningHoursId;

	private Short weekDay;

	public boolean isScheduledForTheFuture() {
		return date.isAfter(LocalDate.now());
	}

	public boolean hasChangedOpeningHours() {
		return !openingHoursId.equals(newOpeningHoursId);
	}
}
