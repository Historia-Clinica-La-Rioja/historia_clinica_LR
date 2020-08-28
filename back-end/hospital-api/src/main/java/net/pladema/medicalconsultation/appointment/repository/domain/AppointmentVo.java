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

	private final Appointment appointment;

	private final Short medicalAttentionTypeId;

	private final String stateChangeReason;

	public AppointmentVo(Appointment appointment) {
		this.appointment = appointment;
		this.medicalAttentionTypeId = null;
		this.stateChangeReason = null;
	}

	public AppointmentVo(Appointment appointment, String stateChangeReason) {
		this.appointment = appointment;
		this.stateChangeReason = stateChangeReason;
		this.medicalAttentionTypeId = null;
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

	public String getMedicalCoverageName() {
		if (appointment == null)
			return null;
		return appointment.getMedicalCoverageName();
	}

	public String getMedicalCoverageAffiliateNumber() {
		if (appointment == null)
			return null;
		return appointment.getMedicalCoverageAffiliateNumber();
	}

	public Integer getHealthInsuranceId() {
		if (appointment == null)
			return null;
		return appointment.getHealthInsuranceId();
	}
}
