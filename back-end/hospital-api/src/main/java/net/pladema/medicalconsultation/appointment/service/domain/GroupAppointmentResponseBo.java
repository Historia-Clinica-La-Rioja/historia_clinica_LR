package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GroupAppointmentResponseBo {

	private Integer appointmentId;
	private String patientFullName;
	private String identificationNumber;
	private Integer patientId;
	private Short appointmentStateId;
	private Integer personId;

	public GroupAppointmentResponseBo(Integer appointmentId, String identificationNumber, Integer patientId, Short appointmentStateId, Integer personId) {
		this.appointmentId = appointmentId;
		this.identificationNumber = identificationNumber;
		this.patientId = patientId;
		this.appointmentStateId = appointmentStateId;
		this.personId = personId;
	}
}
