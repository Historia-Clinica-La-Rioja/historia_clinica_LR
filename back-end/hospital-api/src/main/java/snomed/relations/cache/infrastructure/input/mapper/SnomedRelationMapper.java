package snomed.relations.cache.infrastructure.input.mapper;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import org.mapstruct.Named;

import snomed.relations.cache.domain.SnomedBo;

import java.util.List;

@Mapper
public interface SnomedRelationMapper {

	@Named("toSharedSnomedDto")
	SharedSnomedDto toSharedSnomedDto(SnomedBo snomedBo);

	@IterableMapping(qualifiedByName = "toSharedSnomedDto")
	@Named("toSharedSnomedDtoList")
	List<SharedSnomedDto> toSharedSnomedDtoList(List<SnomedBo> snomedBos);

}
