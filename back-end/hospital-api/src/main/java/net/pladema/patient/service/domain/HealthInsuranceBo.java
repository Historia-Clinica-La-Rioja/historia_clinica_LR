package net.pladema.patient.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.controller.dto.CoverageDto;
import net.pladema.patient.controller.dto.HealthInsuranceDto;
import net.pladema.person.repository.entity.HealthInsurance;

@Getter
@Setter
@NoArgsConstructor
public class HealthInsuranceBo extends MedicalCoverageBo {

    private Integer rnos;
    private String acronym;

    public HealthInsuranceBo(Integer id, String name, String cuit, Integer rnos, String acronym, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
        this.rnos = rnos;
        this.acronym = acronym;
    }

	@Override
    public CoverageDto newInstance() {
        return new HealthInsuranceDto(getId(), getName(), getCuit(), getRnos(), getAcronym(), getType());
    }

    @Override
    public HealthInsurance mapToEntity() {
        return new HealthInsurance(getId(), getName(), getCuit(), getRnos(), getAcronym(), getType());
    }

}
