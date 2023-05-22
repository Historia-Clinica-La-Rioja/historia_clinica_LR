package net.pladema.patient.infrastructure.output.notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PatientRecipient {
	public final Integer patientId;
	public final String email;

	public PatientRecipient(Integer patientId){
		this.patientId = patientId;
		this.email = null;
	}
}
