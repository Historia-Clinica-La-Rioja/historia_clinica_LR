package net.pladema.template.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.template.domain.ConclusionTemplateBo;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.infrastructure.input.rest.dto.ConclusionTemplateDto;
import net.pladema.template.infrastructure.input.rest.dto.DocumentTemplateDto;
import net.pladema.template.infrastructure.input.rest.dto.TemplateNamesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface DocumentTemplateMapper {
	@Named("toDocumentTemplateBo")
	DocumentTemplateBo toDocumentTemplateBo(DocumentTemplateDto documentTemplateDto);

	@Named("toConclusionTemplateBo")
	ConclusionTemplateBo toConclusionTemplateBo(ConclusionTemplateDto conclusionTemplateDto);

	@Named("toTemplateNamesDto")
	@Mapping(target = "id", source = "id")
	@Mapping(target = "name", source = "name")
	TemplateNamesDto toTemplateNamesDto(DocumentTemplateBo documentTemplateBo);
}
