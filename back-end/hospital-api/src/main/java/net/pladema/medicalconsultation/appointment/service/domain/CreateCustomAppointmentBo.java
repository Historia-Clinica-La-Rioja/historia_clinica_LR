package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomAppointmentBo {

	private AppointmentBo createAppointmentBo;
	private CustomRecurringAppointmentBo customRecurringAppointmentBo;

	public LocalDate getDate() {
		return createAppointmentBo.getDate();
	}

	public Short getRepeatEvery() {
		return customRecurringAppointmentBo.getRepeatEvery();
	}

	public LocalDate getEndDate() {
		return customRecurringAppointmentBo.getEndDate();
	}

	public Integer getDiaryId() {
		return createAppointmentBo.getDiaryId();
	}

	public Integer getOpeningHours() {
		return createAppointmentBo.getOpeningHoursId();
	}

	public LocalTime getHour() {
		return createAppointmentBo.getHour();
	}

	public boolean isOverturn() {
		return createAppointmentBo.isOverturn();
	}
}
