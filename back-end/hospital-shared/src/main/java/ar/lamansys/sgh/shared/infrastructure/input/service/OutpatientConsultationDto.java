package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class OutpatientConsultationDto {

	private Integer id;

	private BasicPatientDto patient;

	private String date;

	private String clinicalSpecialtySctid;

	private String institutionSisaCode;

	private SharedAnthropometricDataDto anthropometricData;

	private SharedRiskFactorDto riskFactor;

	private List<SharedSnomedDto> problems;

	private List<SharedSnomedDto> procedures;

	private List<SharedSnomedDto> medications;
}
