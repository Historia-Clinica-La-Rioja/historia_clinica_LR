package net.pladema.snowstorm.infrastructure.input.rest.mapper;

import net.pladema.snowstorm.infrastructure.input.rest.dto.SnomedRelatedGroupDto;
import net.pladema.snowstorm.services.domain.SnomedRelatedGroupBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface SnomedRelatedGroupMapper {

	@Named("fromSnomedRelatedGroupDto")
	SnomedRelatedGroupBo fromSnomedRelatedGroupDto(SnomedRelatedGroupDto snomedGroupDto);

	@Named("fromSnomedRelatedGroupBo")
	SnomedRelatedGroupDto fromSnomedRelatedGroupBo(SnomedRelatedGroupBo snomedGroupBo);

}
