package ar.lamansys.sgh.shared.infrastructure.input.service.interoperability.cipres;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAnthropometricDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedRiskFactorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
public class CipresOutpatientConsultationDto {

	private Integer id;

	private BasicPatientDto patient;

	private String date;

	private Integer clinicalSpecialtyId;

	private String clinicalSpecialtySctid;

	private Integer institutionId;

	private String institutionSisaCode;

	private Integer cipresEncounterId;

	private SharedAnthropometricDataDto anthropometricData;

	private SharedRiskFactorDto riskFactor;

	private List<SharedSnomedDto> problems;

	private List<SharedSnomedDto> procedures;

	private List<SharedSnomedDto> medications;
}
