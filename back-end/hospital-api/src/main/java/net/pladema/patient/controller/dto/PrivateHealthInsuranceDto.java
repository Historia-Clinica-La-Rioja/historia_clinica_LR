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

    public PrivateHealthInsuranceDto(Integer id, String name, String cuit, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
    }

	public Short getType(){
		return EMedicalCoverageType.PREPAGA.getId();
	}

	@Override
    public MedicalCoverageBo newInstance() {
        return new PrivateHealthInsuranceBo(getId(), getName(),getCuit(), getType());
    }
}
