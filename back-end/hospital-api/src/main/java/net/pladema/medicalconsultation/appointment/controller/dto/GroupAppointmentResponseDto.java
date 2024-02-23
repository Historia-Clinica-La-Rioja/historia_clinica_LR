package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class GroupAppointmentResponseDto {

	private Integer appointmentId;
	private String patientFullName;
	private String identificationNumber;
	private Integer patientId;
	private Short appointmentStateId;
}
