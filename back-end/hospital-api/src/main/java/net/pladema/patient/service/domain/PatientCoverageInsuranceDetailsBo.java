package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.patient.controller.dto.CoverageDto;
import net.pladema.patient.repository.entity.MedicalCoverage;

@Getter
@Setter
@AllArgsConstructor
public class PatientCoverageInsuranceDetailsBo extends MedicalCoverageBo{

	private Integer id;
	private String name;
	private String cuit;
	private String coverageType;

	@Override
	public String obtainCoverageType() {
		return this.coverageType;
	}

	@Override
	public CoverageDto newInstance() {
		// nothing to do
		return null;
	}

	@Override
	public MedicalCoverage mapToEntity() {
		// nothing to do
		return null;
	}
}
