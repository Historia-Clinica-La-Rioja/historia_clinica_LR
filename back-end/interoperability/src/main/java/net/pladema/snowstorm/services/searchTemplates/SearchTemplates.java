package net.pladema.snowstorm.services.searchTemplates;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.domain.SnomedTemplateSearchVo;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.SnomedTemplateSearchItemBo;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchTemplates {

	private final SnomedSemantics snomedSemantics;
	private final SnomedGroupRepository snomedGroupRepository;

	public List<SnomedTemplateSearchItemBo> run(String term, String eclKey, Integer institutionId) {
		log.debug("Input parameters -> term {}, eclKey {}, institutionId {}", term, eclKey, institutionId);
		String ecl = snomedSemantics.getEcl(SnomedECL.map(eclKey));
		Integer userId = SecurityContextUtils.getUserDetails().getUserId();
		List<SnomedTemplateSearchVo> queryResult = (term != null) ?
				snomedGroupRepository.searchByTemplateName(term, ecl, eclKey, institutionId, userId)
				: snomedGroupRepository.getTemplates(ecl, eclKey, institutionId, userId);
		List<SnomedTemplateSearchItemBo> result = groupConceptsByTemplate(queryResult);
		log.debug("Output size -> {}", result.size());
		log.trace("Output -> {}", result);
		return result;
	}

	private List<SnomedTemplateSearchItemBo> groupConceptsByTemplate(List<SnomedTemplateSearchVo> snomedTemplateSearchVos) {
		log.debug("Input parameter -> snomedTemplateSearchVos size = {}", snomedTemplateSearchVos.size());
		log.trace("Input parameter -> snomedTemplateSearchVos {}", snomedTemplateSearchVos);
		List<SnomedTemplateSearchItemBo> result = new ArrayList<>();
		snomedTemplateSearchVos.stream()
				.collect(Collectors.groupingBy(SnomedTemplateSearchVo::getGroupId))
				.forEach((groupId, templateSearch) -> {
					String groupDescription = templateSearch.get(0).getDescription();
					List<SnomedSearchItemBo> items = mapToSnomedSearchItemList(templateSearch);
					result.add(new SnomedTemplateSearchItemBo(groupId, groupDescription, items));
				});
		log.debug("Output size -> {}", result.size());
		log.trace("Output -> {}", result);
		return result;
	}

	private List<SnomedSearchItemBo> mapToSnomedSearchItemList(List<SnomedTemplateSearchVo> templateSearch) {
		return templateSearch
				.stream()
				.map(SnomedSearchItemBo::new)
				.collect(Collectors.toList());
	}

}
