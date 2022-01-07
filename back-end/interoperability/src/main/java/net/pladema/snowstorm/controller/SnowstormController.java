package net.pladema.snowstorm.controller;

import io.swagger.annotations.Api;
import net.pladema.snowstorm.controller.dto.SnomedEclDto;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.FetchAllSnomedEcl;
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
@Api(value = "Snowstorm", tags = { "Snowstorm" })
public class SnowstormController {

    private static final String CONCEPTS = "/concepts";

    private static final Logger LOG = LoggerFactory.getLogger(SnowstormController.class);

    public static final String OUTPUT = "Output -> {}";

    private final SnowstormService snowstormService;

    private final FetchAllSnomedEcl fetchAllSnomedEcl;

    public SnowstormController(SnowstormService snowstormService, FetchAllSnomedEcl fetchAllSnomedEcl){
        this.snowstormService = snowstormService;
        this.fetchAllSnomedEcl = fetchAllSnomedEcl;
    }

    @GetMapping(value = CONCEPTS)
    public SnowstormSearchResponse getConcepts(
            @RequestParam(value = "term") String term,
            @RequestParam(value = "ecl", required = false) String eclKey) throws SnowstormApiException {
        LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
        return snowstormService.getConcepts(term, eclKey);
    }

    @GetMapping(value = "/ecl")
    public List<SnomedEclDto> getEcls() {
        return fetchAllSnomedEcl.run().stream()
                .map(snomedECLBo -> new SnomedEclDto(snomedECLBo.getKey(), snomedECLBo.getValue()))
                .collect(Collectors.toList());
    }
}
