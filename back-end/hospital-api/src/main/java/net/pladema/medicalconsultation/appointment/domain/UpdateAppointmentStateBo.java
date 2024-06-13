package net.pladema.medicalconsultation.appointment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateAppointmentStateBo {

	private Integer appointmentId;

	private Short appointmentStateId;

	private String reason;

	private String patientIdentificationBarCode;

}
