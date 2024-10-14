package net.pladema.medicalconsultation.appointment.infrastructure.input.rest.dto;

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
public class UpdateAppointmentStateDto {

	private String reason;

	private String patientIdentificationBarCode;

}
