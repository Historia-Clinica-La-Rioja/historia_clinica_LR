package net.pladema.snowstorm.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.domain.SnomedBo;
import net.pladema.snowstorm.application.fetchconcepts.FetchConcepts;
import net.pladema.snowstorm.application.fetchconceptswithresultcount.FetchConceptsWithResultCount;
import net.pladema.snowstorm.controller.dto.FullySpecifiedNamesDto;
import net.pladema.snowstorm.controller.dto.PreferredTermDto;
import net.pladema.snowstorm.controller.dto.SnomedEclDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchItemDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchDto;
import net.pladema.snowstorm.controller.dto.SnomedTemplateDto;
import net.pladema.snowstorm.domain.GetSnomedConceptEclRelated;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.SnomedTemplateSearchItemBo;
import net.pladema.snowstorm.services.domain.FetchAllSnomedEcl;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConcepts;
import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConceptsWithResultCount;
import net.pladema.snowstorm.services.searchTemplates.SearchTemplates;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Tag(name = "Snowstorm", description = "Snowstorm")
@RequestMapping("/snowstorm")
@RestController
public class SnowstormController {

    private static final String CONCEPTS = "/concepts";

	private static final String OUTPUT = "Output -> {}";

	private static final String INPUT_TERM_ECL = "Input data -> term: {}, ecl: {} ";

    private final FetchAllSnomedEcl fetchAllSnomedEcl;

	private final SearchTemplates searchTemplates;

	private ObjectMapper objectMapper;

	private GetSnomedConceptEclRelated getSnomedConceptEclRelated;
	
	private final FetchConcepts fetchConcepts;

	private final FetchConceptsWithResultCount fetchConceptsWithResultCount;

	private final SnomedSearchMapper snomedSearchMapper;

    @GetMapping(value = CONCEPTS)
    public SnomedSearchDto getConceptsWithResultCount(
            @RequestParam(value = "term") String term,
            @RequestParam(value = "ecl", required = false) String eclKey) throws SnowstormApiException {
        log.debug(INPUT_TERM_ECL, term, eclKey);
		var snomedSearchBo = fetchConceptsWithResultCount.run(term, eclKey);
        SnomedSearchDto result = snomedSearchMapper.toSnomedSearchDto(snomedSearchBo);
        log.debug(OUTPUT, result);
        return result;
    }

	@GetMapping(value = "/search-concepts")
	public List<SnomedSearchItemDto> getConcepts(@RequestParam(value = "term") String term,
												 @RequestParam(value = "ecl", required = false) String eclKey) throws SnowstormApiException {
		log.debug(INPUT_TERM_ECL, term, eclKey);
		var concepts = fetchConcepts.run(term, eclKey);
		List<SnomedSearchItemDto> result = snomedSearchMapper.toSnomedSearchItemDtoList(concepts);
		log.debug("Output size -> {}", result.size());
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping(value = "/concept-related-ecl")
	public List<SharedSnomedDto> areConceptsECLRelated(@RequestParam(value = "snomedConcepts") String snomedConcepts,
													   @Param("ecl") SnomedECL ecl) throws JsonProcessingException {
		log.debug("Input parameters -> snomedConcepts {}, ecl {}", snomedConcepts, ecl);
		List<SharedSnomedDto> parsedSnomeds = objectMapper.readValue(snomedConcepts, objectMapper.getTypeFactory().constructCollectionType(List.class, SharedSnomedDto.class));
		List<SnomedBo> snomedBos = parsedSnomeds.stream().map(snomed -> new SnomedBo(snomed.getSctid(), snomed.getPt())).collect(Collectors.toList());
		List<SharedSnomedDto> result = getSnomedConceptEclRelated.run(snomedBos, ecl).stream().map(concept -> new SharedSnomedDto(concept.getSctid(), concept.getPt())).collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping(value = "/search-templates")
	public List<SnomedTemplateDto> searchSnomedTemplates(
			@RequestParam(value = "term", required = false) String term,
			@RequestParam(value = "ecl") String eclKey,
			@RequestParam(value = "institutionId") Integer institutionId) {
		log.debug("Input data -> term: {} , eclKey: {}, institutionId: {}", term, eclKey, institutionId);
		List<SnomedTemplateDto> result = searchTemplates.run(term, eclKey, institutionId)
				.stream()
				.map(this::mapToSnomedTemplateDto)
				.sorted(Comparator.comparing(SnomedTemplateDto::getDescription))
				.collect(Collectors.toList());
		log.debug("Output size -> {}", result.size());
		log.debug(OUTPUT, result);
		return result;
	}

    @GetMapping(value = "/ecl")
    public List<SnomedEclDto> getEcls() {
        return fetchAllSnomedEcl.run().stream()
                .map(snomedECLBo -> new SnomedEclDto(snomedECLBo.getKey(), snomedECLBo.getValue()))
                .collect(Collectors.toList());
    }

	private SnomedTemplateDto mapToSnomedTemplateDto(SnomedTemplateSearchItemBo snomedTemplateSearchItemBo) {
		List<SnomedSearchItemDto> items = snomedTemplateSearchItemBo.getConcepts()
				.stream()
				.map(this::mapToSnomedSearchItemDto)
				.collect(Collectors.toList());
		return new SnomedTemplateDto(snomedTemplateSearchItemBo.getDescription(), items);
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
