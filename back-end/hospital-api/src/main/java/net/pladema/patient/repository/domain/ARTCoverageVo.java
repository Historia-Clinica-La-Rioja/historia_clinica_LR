package net.pladema.patient.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.service.domain.ARTCoverageBo;
import net.pladema.patient.service.domain.MedicalCoverageBo;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ARTCoverageVo extends MedicalCoverageVo {

    public ARTCoverageVo(Integer id, String name, String cuit, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new ARTCoverageBo(getId(), getName(), getCuit(), getType());
    }
}

