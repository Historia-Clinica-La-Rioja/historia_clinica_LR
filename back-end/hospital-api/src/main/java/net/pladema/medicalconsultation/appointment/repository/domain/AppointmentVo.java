package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;
import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@ToString
@AllArgsConstructor
public class AppointmentVo {

	private Appointment appointment;

	private Short medicalAttentionTypeId;

	private String stateChangeReason;
	
    private Integer diaryId;

	public AppointmentVo(Appointment appointment) {
		this.appointment = appointment;
		this.medicalAttentionTypeId = null;
		this.stateChangeReason = null;
		this.diaryId = null;
	}

	public AppointmentVo(Integer diaryId, Appointment appointment, Short medicalAttentionTypeId, String stateChangeReason) {
		this.appointment = appointment;
		this.stateChangeReason = stateChangeReason;
		this.medicalAttentionTypeId = medicalAttentionTypeId;
		this.diaryId = diaryId;
	}

	public Integer getId() {
		return appointment.getId();
	}

	public Integer getPatientId() {
		if (appointment == null)
			return null;
		return appointment.getPatientId();
	}
	
	public LocalDate getDate() {
		if (appointment == null)
			return null;
		return appointment.getDateTypeId();
	}

	public LocalTime getHour() {
		if (appointment == null)
			return null;
		return appointment.getHour();
	}

	public Short getAppointmentStateId() {
		if (appointment == null)
			return null;
		return appointment.getAppointmentStateId();
	}

	public boolean isOverturn() {
		if (appointment == null)
			return false;
		return appointment.getIsOverturn();
	}

	public Integer getPatientMedicalCoverageId(){
		if (appointment == null)
			return null;
		return appointment.getPatientMedicalCoverageId();
	}
}
