package net.pladema.snowstorm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnvsToReportDto {

	private Integer groupEventId;

	private Integer eventId;

	private Integer manualClassificationId;

	private Integer patientId;

	private Integer professionalId;

	private String institutionSisaCode;
}
