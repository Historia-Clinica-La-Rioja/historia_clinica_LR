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

    public HealthInsuranceDto(Integer id, String name, Integer rnos, String acronym) {
        setId(id);
        setName(name);
        this.rnos = rnos;
        this.acronym = acronym;
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new HealthInsuranceBo(getId(), getName(), getRnos(), getAcronym());
    }
}
