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

    public HealthInsuranceBo(Integer id, String name, Integer rnos, String acronym){
        setId(id);
        setName(name);
        this.rnos = rnos;
        this.acronym = acronym;
    }

    @Override
    public CoverageDto newInstance() {
        return new HealthInsuranceDto(getId(), getName(), getRnos(), getAcronym());
    }

    @Override
    public HealthInsurance mapToEntity() {
        return new HealthInsurance(getId(), getName(), getRnos(), getAcronym());
    }

}
