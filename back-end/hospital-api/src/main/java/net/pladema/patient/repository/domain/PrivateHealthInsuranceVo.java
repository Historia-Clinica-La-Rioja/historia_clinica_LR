package net.pladema.patient.repository.domain;

import lombok.*;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PrivateHealthInsuranceVo extends MedicalCoverageVo {

    public PrivateHealthInsuranceVo(Integer id, String name, String cuit){
        super(id, name, cuit);
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new PrivateHealthInsuranceBo(getId(), getName(), getCuit());
    }
}
