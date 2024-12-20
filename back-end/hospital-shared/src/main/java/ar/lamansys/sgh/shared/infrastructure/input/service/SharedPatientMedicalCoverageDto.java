package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SharedPatientMedicalCoverageDto {
	private String name;
	private String type;
	private String plan;
	private String affiliateNumber;
	private String affiliationType;
	private String cuit;
	private String startDate;
	private String endDate;
}
