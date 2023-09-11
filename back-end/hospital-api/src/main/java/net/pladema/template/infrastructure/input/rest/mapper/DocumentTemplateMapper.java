package net.pladema.template.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.domain.TextTemplateBo;
import net.pladema.template.infrastructure.input.rest.dto.TemplateNamesDto;
import net.pladema.template.infrastructure.input.rest.dto.TextTemplateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface DocumentTemplateMapper {

	@Named("toTextTemplateBo")
	TextTemplateBo toTextTemplateBo(TextTemplateDto textTemplateDto);

	@Named("toTemplateNamesDto")
	@Mapping(target = "id", source = "id")
	@Mapping(target = "name", source = "name")
	TemplateNamesDto toTemplateNamesDto(DocumentTemplateBo documentTemplateBo);

	@Named("toTextTemplateDto")
	@Mapping(target = "text", source = "text")
    TextTemplateDto toTextTemplateDto(TextTemplateBo textTemplateBo);
}
