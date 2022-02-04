package net.pladema.snowstorm.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.snowstorm.controller.dto.FullySpecifiedNamesDto;
import net.pladema.snowstorm.controller.dto.PreferredTermDto;
import net.pladema.snowstorm.controller.dto.SnomedEclDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchItemDto;
import net.pladema.snowstorm.controller.dto.SnomedSearchDto;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.FetchAllSnomedEcl;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/snowstorm")
@Tag(name = "Snowstorm", description = "Snowstorm")
public class SnowstormController {

    private static final String CONCEPTS = "/concepts";
    private static final Logger LOG = LoggerFactory.getLogger(SnowstormController.class);
    private final SnowstormService snowstormService;

    private final FetchAllSnomedEcl fetchAllSnomedEcl;

    public SnowstormController(SnowstormService snowstormService, FetchAllSnomedEcl fetchAllSnomedEcl) {
        this.snowstormService = snowstormService;
        this.fetchAllSnomedEcl = fetchAllSnomedEcl;
    }

    @GetMapping(value = CONCEPTS)
    public SnomedSearchDto getConcepts(
            @RequestParam(value = "term") String term,
            @RequestParam(value = "ecl", required = false) String eclKey) throws SnowstormApiException {
        LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
        SnowstormSearchResponse response = snowstormService.getConcepts(term, eclKey);
        List<SnomedSearchItemDto> itemList = mapToSnomedSearchItemDtoList(response.getItems());
        SnomedSearchDto result = new SnomedSearchDto(itemList, response.getTotal());
        LOG.debug("Output -> {}", result);
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
}
