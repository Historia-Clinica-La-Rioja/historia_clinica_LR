package net.pladema.patient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.controller.dto.ARTCoverageDto;
import net.pladema.patient.controller.dto.CoverageDto;
import net.pladema.patient.repository.entity.MedicalCoverage;

@Getter
@Setter
@NoArgsConstructor
public class ARTCoverageBo extends MedicalCoverageBo {

	public ARTCoverageBo(Integer id, String name, String cuit, Short type) {
		setId(id);
		setName(name);
		setCuit(cuit);
		setType(type);
	}

	@Override
	public CoverageDto newInstance() {
		return new ARTCoverageDto(getId(), getName(), getCuit(), getType());
	}

	@Override
	public MedicalCoverage mapToEntity() {
		return new MedicalCoverage(getId(), getName(), getCuit(), getType());
	}
}
