package net.pladema.reports.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AnnexIIProcedureDto {
	private String codeNomenclator;
	private String descriptionNomenclator;
	private String description;
	private Float rate;
	private Integer amount;
	private Float patientRate;

}
