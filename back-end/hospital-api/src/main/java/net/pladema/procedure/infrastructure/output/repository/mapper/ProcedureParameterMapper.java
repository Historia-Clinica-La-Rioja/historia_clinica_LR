package net.pladema.procedure.infrastructure.output.repository.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.procedure.domain.ProcedureParameterVo;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface ProcedureParameterMapper {

	@Named("toProcedureParameterDto")
	ProcedureParameterDto toProcedureParameterDto(ProcedureParameterVo procedureParameterVo);

	@Named("toProcedureParameterVo")
	ProcedureParameterVo toProcedureParameterVo(ProcedureParameterDto procedureParameterDto);


}
