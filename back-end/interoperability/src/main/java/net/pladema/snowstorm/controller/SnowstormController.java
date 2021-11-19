package net.pladema.snowstorm.controller;

import io.swagger.annotations.Api;
import net.pladema.snowstorm.controller.dto.ManualClassificationDto;
import net.pladema.snowstorm.controller.dto.SnomedEclDto;
import net.pladema.snowstorm.controller.dto.SnvsReportDto;
import net.pladema.snowstorm.controller.dto.SnvsToReportDto;
import net.pladema.snowstorm.controller.mapper.ManualClassificationMapper;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.FetchAllSnomedEcl;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.domain.SnvsToReportBo;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    private static final String REPORTABLE = "/is-reportable";

    private static final String REPORT = "/report";

    private static final Logger LOG = LoggerFactory.getLogger(SnowstormController.class);

    public static final String OUTPUT = "Output -> {}";

    private final SnowstormService snowstormService;

    private final FetchAllSnomedEcl fetchAllSnomedEcl;

    private final ManualClassificationMapper manualClassificationMapper;

    public SnowstormController(SnowstormService snowstormService, FetchAllSnomedEcl fetchAllSnomedEcl,
                               ManualClassificationMapper manualClassificationMapper){
        this.snowstormService = snowstormService;
        this.fetchAllSnomedEcl = fetchAllSnomedEcl;
        this.manualClassificationMapper = manualClassificationMapper;
    }

    @GetMapping(value = CONCEPTS)
    public SnowstormSearchResponse getConcepts(
            @RequestParam(value = "term") String term,
            @RequestParam(value = "ecl", required = false) String eclKey) throws SnowstormApiException {
        LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
        return snowstormService.getConcepts(term, eclKey);
    }

    @GetMapping(value = REPORTABLE)
    public ResponseEntity<List<ManualClassificationDto>> isSnvsReportable(
            @RequestParam(value = "sctid") String sctid,
            @RequestParam(value = "pt") String pt) {
        LOG.debug("Input data -> sctid: {} , pt: {} ", sctid, pt);
        List<ManualClassificationBo> resultService = snowstormService.isSnvsReportable(sctid, pt);
        List<ManualClassificationDto> result = manualClassificationMapper.fromManualClassificationBoList(resultService);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = REPORT)
    public ResponseEntity<List<SnvsReportDto>> reportSnvs(
            @RequestParam(value = "toReportList", required = true) List<SnvsToReportDto> toReportList) {
        List<SnvsToReportBo> resultService = snowstormService.tryReportAndSave(toReportList);
        //List<SnvsToReportDto> result = mapper.fromSnvsToReportBoList(resultService);
        LOG.debug(OUTPUT, resultService);
        return ResponseEntity.ok().body(null);
    }


    @GetMapping(value = "/ecl")
    public List<SnomedEclDto> getEcls() {
        return fetchAllSnomedEcl.run().stream()
                .map(snomedECLBo -> new SnomedEclDto(snomedECLBo.getKey(), snomedECLBo.getValue()))
                .collect(Collectors.toList());
    }
}
