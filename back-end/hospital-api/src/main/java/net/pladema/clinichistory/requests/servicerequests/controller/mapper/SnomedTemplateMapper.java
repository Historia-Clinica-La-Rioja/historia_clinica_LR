package net.pladema.clinichistory.requests.servicerequests.controller.mapper;

import net.pladema.snowstorm.controller.dto.FullySpecifiedNamesDto;
import net.pladema.snowstorm.controller.dto.PreferredTermDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchItemDto;
import net.pladema.snowstorm.controller.dto.SnomedTemplateDto;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.SnomedTemplateSearchItemBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnomedTemplateMapper {

	public SnomedTemplateDto mapToSnomedTemplateDto(SnomedTemplateSearchItemBo snomedTemplateSearchItemBo) {
		List<SnomedSearchItemDto> items = snomedTemplateSearchItemBo.getConcepts()
				.stream()
				.map(this::mapToSnomedSearchItemDto)
				.collect(Collectors.toList());
		return new SnomedTemplateDto(snomedTemplateSearchItemBo.getGroupId(), snomedTemplateSearchItemBo.getDescription(), items);
	}

	private SnomedSearchItemDto mapToSnomedSearchItemDto(SnomedSearchItemBo snomedCachedSearchBo) {
		SnomedSearchItemDto result = new SnomedSearchItemDto();
		result.setConceptId(snomedCachedSearchBo.getSctid());
		result.setId(snomedCachedSearchBo.getSctid());
		result.setPt(new PreferredTermDto(snomedCachedSearchBo.getPt(), "es"));
		result.setFsn(new FullySpecifiedNamesDto(snomedCachedSearchBo.getPt(), "es"));
		return result;
	}
}
