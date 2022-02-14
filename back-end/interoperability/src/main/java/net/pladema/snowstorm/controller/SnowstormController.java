package net.pladema.snowstorm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.snowstorm.controller.dto.FullySpecifiedNamesDto;
import net.pladema.snowstorm.controller.dto.PreferredTermDto;
import net.pladema.snowstorm.controller.dto.SnomedEclDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchItemDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchDto;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv;
import net.pladema.snowstorm.services.domain.FetchAllSnomedEcl;
import net.pladema.snowstorm.services.domain.SnomedSearchBo;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConcepts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/snowstorm")
@Tag(name = "Snowstorm", description = "Snowstorm")
public class SnowstormController {

    @Value("${ws.snowstorm.searchLocally.enabled:false}")
    private boolean searchConceptsLocallyEnabled;

    private static final String CONCEPTS = "/concepts";

    private static final Logger LOG = LoggerFactory.getLogger(SnowstormController.class);

    private final SnowstormService snowstormService;

    private final FetchAllSnomedEcl fetchAllSnomedEcl;

    private final SearchCachedConcepts searchCachedConcepts;

    private final UpdateSnomedConceptsByCsv updateSnomedConceptsByCsv;

    public SnowstormController(SnowstormService snowstormService,
                               FetchAllSnomedEcl fetchAllSnomedEcl,
                               SearchCachedConcepts searchCachedConcepts,
                               UpdateSnomedConceptsByCsv updateSnomedConceptsByCsv) {
        this.snowstormService = snowstormService;
        this.fetchAllSnomedEcl = fetchAllSnomedEcl;
        this.searchCachedConcepts = searchCachedConcepts;
        this.updateSnomedConceptsByCsv = updateSnomedConceptsByCsv;
    }

    @GetMapping(value = CONCEPTS)
    public SnomedSearchDto getConcepts(
            @RequestParam(value = "term") String term,
            @RequestParam(value = "ecl", required = false) String eclKey) throws SnowstormApiException {
        LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
        SnomedSearchDto result;
        if (!searchConceptsLocallyEnabled) {
			result = searchInSnowstorm(term, eclKey);
		} else {
			result = searchLocally(term, eclKey);
		}
        LOG.debug("Output -> {}", result);
        return result;
    }

	private SnomedSearchDto searchLocally(String term, String eclKey) {
		LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
		SnomedSearchDto result;
		SnomedSearchBo searchResult = searchCachedConcepts.run(term, eclKey);
		List<SnomedSearchItemDto> items =
				searchResult.getItems()
						.stream()
						.map(this::mapToSnomedSearchItemDto)
						.collect(Collectors.toList());
		result = new SnomedSearchDto(items, searchResult.getTotalMatches());
		return result;
	}

	private SnomedSearchDto searchInSnowstorm(String term, String eclKey) throws SnowstormApiException {
		LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
		SnomedSearchDto result;
		SnowstormSearchResponse response = snowstormService.getConcepts(term, eclKey);
		List<SnomedSearchItemDto> itemList = mapToSnomedSearchItemDtoList(response.getItems());
		result = new SnomedSearchDto(itemList, response.getTotal());
		return result;
	}

	private SnomedSearchItemDto mapToSnomedSearchItemDto(SnomedSearchItemBo snomedCachedSearchBo) {
        SnomedSearchItemDto result = new SnomedSearchItemDto();
        result.setConceptId(snomedCachedSearchBo.getSctid());
        result.setId(snomedCachedSearchBo.getSctid());
        result.setPt(new PreferredTermDto(snomedCachedSearchBo.getPt(), "es"));
        result.setFsn(new FullySpecifiedNamesDto(snomedCachedSearchBo.getPt(), "es"));
        return result;
    }

    private List<SnomedSearchItemDto> mapToSnomedSearchItemDtoList(List<SnowstormItemResponse> items) {
        return items.stream()
                .map(i -> new SnomedSearchItemDto(i.getConceptId(),
                        i.getConceptId(),
                        new FullySpecifiedNamesDto(i.getFsn().get("term").textValue(), i.getFsn().get("lang").textValue()),
                        new PreferredTermDto(i.getPt().get("term").textValue(), i.getFsn().get("lang").textValue())))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/ecl")
    public List<SnomedEclDto> getEcls() {
        return fetchAllSnomedEcl.run().stream()
                .map(snomedECLBo -> new SnomedEclDto(snomedECLBo.getKey(), snomedECLBo.getValue()))
                .collect(Collectors.toList());
    }

    @PostMapping("/load-concepts-csv")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void loadConceptsByCsv(@RequestParam("file") MultipartFile file,
						   @RequestParam(value = "ecl") String eclKey) {
        LOG.debug("Input parameters -> file {}, eclKey {}", file.getOriginalFilename(), eclKey);
        updateSnomedConceptsByCsv.run(file, eclKey);
    }

}
