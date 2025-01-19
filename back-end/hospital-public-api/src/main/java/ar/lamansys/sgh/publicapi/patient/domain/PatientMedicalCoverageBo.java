package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PatientMedicalCoverageBo {
	private String name;
	private String type;
	private String plan;
	private String affiliateNumber;
	private String affiliationType;
	private String cuit;
	private String startDate;
	private String endDate;
}
