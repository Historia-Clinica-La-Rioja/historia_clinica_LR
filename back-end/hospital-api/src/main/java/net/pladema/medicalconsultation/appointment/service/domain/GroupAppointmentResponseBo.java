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

	public GroupAppointmentResponseBo(Integer appointmentId, String firstName, String lastName, String identificationNumber, Integer patientId, Short appointmentStateId) {
		this.appointmentId = appointmentId;
		this.patientFullName = firstName != null && lastName != null ? firstName.concat(" ").concat(lastName): null;
		this.identificationNumber = identificationNumber;
		this.patientId = patientId;
		this.appointmentStateId = appointmentStateId;
	}
}
