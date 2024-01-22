package net.pladema.reports.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AnnexIIProcedureDto {
	private String code;
	private String description;
	private Float rate;
	private Integer amount;
	private Float patientRate;

}
