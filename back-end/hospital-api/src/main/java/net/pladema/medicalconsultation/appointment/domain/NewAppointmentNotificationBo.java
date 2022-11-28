package net.pladema.medicalconsultation.appointment.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NewAppointmentNotificationBo {
	public final Integer patientId;
	public final Integer patientMedicalCoverageId;
	public final LocalDate dateTypeId;
	public final LocalTime hour;
	public final Integer diaryId;
}
