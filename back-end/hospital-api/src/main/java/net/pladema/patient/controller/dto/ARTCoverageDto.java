package net.pladema.patient.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.ARTCoverageBo;
import net.pladema.patient.service.domain.MedicalCoverageBo;

@Setter
@Getter
@NoArgsConstructor
public class ARTCoverageDto extends CoverageDto {

    public ARTCoverageDto(Integer id, String name, String cuit, Short type){
        setId(id);
        setName(name);
        setCuit(cuit);
		setType(type);
    }

	public Short getType(){
		return EMedicalCoverageType.ART.getId();
	}

	@Override
    public MedicalCoverageBo newInstance() {
        return new ARTCoverageBo(getId(), getName(),getCuit(), getType());
    }
}
