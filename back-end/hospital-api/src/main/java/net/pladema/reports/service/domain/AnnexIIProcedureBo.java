package net.pladema.reports.service.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AnnexIIProcedureBo {

	private String code;
	private String description;
	private Integer amount;
	private LocalDateTime date;
	private Float rate;
	private Float coveragePercentage;
	private Float coverageRate;
	private Float patientRate;
	private Float total;

}
