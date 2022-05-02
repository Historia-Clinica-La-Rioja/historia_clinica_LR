package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.HealthInsuranceBo;
import net.pladema.patient.service.domain.MedicalCoverageBo;

@Setter
@Getter
@NoArgsConstructor
public class HealthInsuranceDto extends CoverageDto {

    Integer rnos;

    String acronym;

    public HealthInsuranceDto(Integer id, String name, String cuit, Integer rnos, String acronym) {
        setId(id);
        setName(name);
        setCuit(cuit);
        this.rnos = rnos;
        this.acronym = acronym;
    }

	@Override
	public String obtainCoverageType() {
		return "OBRASOCIAL";
	}

	@Override
    public MedicalCoverageBo newInstance() {
        return new HealthInsuranceBo(getId(), getName(), getCuit(), getRnos(), getAcronym());
    }
}
