package net.pladema.cipres.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class OutpatientConsultationBo {

	private Integer id;

	private BasicDataPatientBo patient;

	private Integer apiPatientId;

	private String date;

	private String clinicalSpecialtySctid;

	private String institutionSisaCode;

	private AnthropometricDataBo anthropometricData;

	private RiskFactorBo riskFactor;

	private List<SnomedBo> problems;

	private List<SnomedBo> procedures;

}
