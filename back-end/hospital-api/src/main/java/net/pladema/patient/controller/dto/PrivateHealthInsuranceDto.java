package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

@Setter
@Getter
@NoArgsConstructor
public class PrivateHealthInsuranceDto extends CoverageDto {

    String plan;

    public PrivateHealthInsuranceDto(Integer id, String name, String cuit, String plan){
        setId(id);
        setName(name);
        setCuit(cuit);
        this.plan=plan;
    }

    @Override
    public MedicalCoverageBo newInstance() {
        return new PrivateHealthInsuranceBo(getId(), getName(),getCuit(), getPlan());
    }
}
