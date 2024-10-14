package net.pladema.parameter.infrastructure.input.rest.mapper;

import jdk.jfr.Name;

import net.pladema.parameter.domain.ParameterCompleteDataBo;
import net.pladema.parameter.infrastructure.input.rest.dto.ParameterCompleteDataDto;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ParameterMapper {

	@Name("toParameterCompleteDataDto")
	List<ParameterCompleteDataDto> toParameterCompleteDataDto(List<ParameterCompleteDataBo> bo);

}
