package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.mapper;

import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicDataBo;
import ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic.AnthropometricGraphicEnablementBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto.AnthropometricGraphicDataDto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.anthropometricgraphic.dto.AnthropometricGraphicEnablementDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface AnthropometricGraphicMapper {

	@Named("toPercentilesGraphicDataDto")
	AnthropometricGraphicDataDto anthropometricGraphicDataDto (AnthropometricGraphicDataBo percentilesGraphicDataBo);

	@Named("toAnthropometricGraphicEnablementDto")
	AnthropometricGraphicEnablementDto toAnthropometricGraphicEnablementDto(AnthropometricGraphicEnablementBo anthropometricGraphicEnablementBo);

}
