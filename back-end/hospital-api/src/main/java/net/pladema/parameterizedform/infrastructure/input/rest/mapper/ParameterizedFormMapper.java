package net.pladema.parameterizedform.infrastructure.input.rest.mapper;

import net.pladema.parameterizedform.domain.ParameterizedFormBo;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ParameterizedFormMapper {

	@Named("toParameterizedFormDtoList")
	List<ParameterizedFormDto> toParameterizedFormDtoList(List<ParameterizedFormBo> parameterizedFormBo);

}
