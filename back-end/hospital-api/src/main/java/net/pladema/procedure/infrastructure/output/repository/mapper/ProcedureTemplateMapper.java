package net.pladema.procedure.infrastructure.output.repository.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import net.pladema.procedure.domain.ProcedureTemplateVo;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureTemplateDto;

import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {LocalDateMapper.class})
public interface ProcedureTemplateMapper {

	@Named("toProcedureTemplateDto")
	@Mapping(target = "associatedPractices", source = "associatedPractices")
	ProcedureTemplateDto toProcedureTemplateDto(ProcedureTemplateVo procedureTemplateVo);

	@Named("toProcedureTemplateBo")
	@Mapping(target = "associatedPractices", source = "associatedPractices")
	ProcedureTemplateVo toProcedureTemplateBo(ProcedureTemplateDto procedureTemplateDto);

	@Named("toListProcedureTemplateDto")
	@IterableMapping(qualifiedByName = "toProcedureTemplateDto")
	List<ProcedureTemplateDto> toListProcedureTemplateDto(List<ProcedureTemplateVo> procedureTemplateVoList);

	ProcedureTemplateVo toProcedureTemplateVo(ProcedureTemplate procedureTemplate);
}
