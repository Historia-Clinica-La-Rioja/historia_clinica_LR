package net.pladema.patient.service.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.patient.controller.dto.CoverageDto;
import net.pladema.patient.repository.entity.MedicalCoverage;

@Getter
@Setter
public class PatientCoverageInsuranceDetailsBo extends MedicalCoverageBo{

	public PatientCoverageInsuranceDetailsBo(Integer id, String name, String cuit, Short type){
		super(id,name,cuit,type);
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
