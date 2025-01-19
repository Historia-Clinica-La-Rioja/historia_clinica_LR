package net.pladema.medicalconsultation.appointment.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DetailsOrderImageDto {
	private String observations;
	private Boolean isReportRequired;
	private Integer patientId;
}
