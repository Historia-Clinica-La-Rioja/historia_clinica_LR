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

    public HealthInsuranceDto(Integer id, String name, String cuit, Integer rnos, String acronym, Short type) {
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
        this.rnos = rnos;
        this.acronym = acronym;
    }

	public Short getType(){
		return EMedicalCoverageType.OBRASOCIAL.getId();
	}

	@Override
    public MedicalCoverageBo newInstance() {
        return new HealthInsuranceBo(getId(), getName(), getCuit(), getRnos(), getAcronym(), getType());
    }
}
