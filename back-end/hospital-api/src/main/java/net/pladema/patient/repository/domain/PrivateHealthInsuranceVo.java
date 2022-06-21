package net.pladema.patient.repository.domain;

import lombok.*;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PrivateHealthInsuranceVo extends MedicalCoverageVo {

    public PrivateHealthInsuranceVo(Integer id, String name, String cuit, Short type){
        super(id, name, cuit, type);
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new PrivateHealthInsuranceBo(getId(), getName(), getCuit(), getType());
    }
}
