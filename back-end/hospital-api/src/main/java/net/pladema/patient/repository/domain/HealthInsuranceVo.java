package net.pladema.patient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.service.domain.HealthInsuranceBo;
import net.pladema.patient.service.domain.MedicalCoverageBo;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HealthInsuranceVo extends MedicalCoverageVo {

    private Integer rnos;

    private String acronym;

    public HealthInsuranceVo(Integer id, String name, String cuit, Integer rnos, String acronym, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
        this.rnos = rnos;
        this.acronym = acronym;
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new HealthInsuranceBo(getId(), getName(), getCuit(), getRnos(), getAcronym(),getType());
    }
}

