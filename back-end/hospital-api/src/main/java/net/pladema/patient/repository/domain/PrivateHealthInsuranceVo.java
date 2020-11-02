package net.pladema.patient.repository.domain;

import lombok.*;
import net.pladema.patient.service.domain.HealthInsuranceBo;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrivateHealthInsuranceVo extends MedicalCoverageVo {

    private String plan;

    public PrivateHealthInsuranceVo(Integer id, String name, String plan){
        super(id, name);
        this.plan = plan;
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new PrivateHealthInsuranceBo(getId(), getName(), getPlan());
    }
}
