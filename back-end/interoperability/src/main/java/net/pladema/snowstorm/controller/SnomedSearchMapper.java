package net.pladema.snowstorm.controller;

import net.pladema.snowstorm.controller.dto.SnomedSearchDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchItemDto;

import net.pladema.snowstorm.domain.SnomedSearchBo;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface SnomedSearchMapper {

	@Named("toSnomedSearchItemDto")
	SnomedSearchItemDto toSnomedSearchItemDto(SnomedSearchItemBo snomedSearchItemBo);

	@Named("toSnomedSearchItemDtoList")
	@IterableMapping(qualifiedByName = "toSnomedSearchItemDto")
	List<SnomedSearchItemDto> toSnomedSearchItemDtoList(List<SnomedSearchItemBo> snomedSearchItemBoList);

	@Named("toSnomedSearchDto")
	@Mapping(target = "total", source = "totalMatches")

	SnomedSearchDto toSnomedSearchDto(SnomedSearchBo snomedSearchBo);
}
