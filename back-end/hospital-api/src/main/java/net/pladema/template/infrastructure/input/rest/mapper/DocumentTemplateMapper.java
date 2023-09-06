package net.pladema.template.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.template.domain.DocumentTemplateBo;
import net.pladema.template.infrastructure.input.rest.dto.DocumentTemplateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(uses = {LocalDateMapper.class})
public interface DocumentTemplateMapper {
	@Named("toDocumentTemplateBo")
	DocumentTemplateBo toDocumentTemplateBo(DocumentTemplateDto documentTemplateDto);
}
