package net.pladema.snowstorm.controller;

import io.swagger.annotations.Api;
import net.pladema.snowstorm.controller.dto.ManualClassificationDto;
import net.pladema.snowstorm.controller.dto.SnomedEclDto;
import net.pladema.snowstorm.controller.mapper.ManualClassificationMapper;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.FetchAllSnomedEcl;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/snowstorm")
@Api(value = "Snowstorm", tags = { "Snowstorm" })
public class SnowstormController {

    private static final String CONCEPTS = "/concepts";

    private static final String REPORTABLE = "/is-reportable";

    private static final String REFSET_MEMBERS = "/refset-members";

    private static final Logger LOG = LoggerFactory.getLogger(SnowstormController.class);

    private static final String TIMEOUT_MSG = "Timeout en WS Snowstorm";

    public static final String OUTPUT = "Output -> {}";

    private final SnowstormService snowstormService;

    private final FetchAllSnomedEcl fetchAllSnomedEcl;
    
    private final ManualClassificationMapper manualClassificationMapper;

    @Value("${ws.snowstorm.request.timeout:15000}")
    private long requestTimeOut;

    public SnowstormController(SnowstormService snowstormService, FetchAllSnomedEcl fetchAllSnomedEcl, ManualClassificationMapper manualClassificationMapper){
        this.snowstormService = snowstormService;
        this.fetchAllSnomedEcl = fetchAllSnomedEcl;
        this.manualClassificationMapper = manualClassificationMapper;
    }

    @GetMapping(value = CONCEPTS)
    public DeferredResult<ResponseEntity<SnowstormSearchResponse>> getConcepts(
            @RequestParam(value = "term", required = true) String term,
            @RequestParam(value = "ecl", required = false) String eclKey) {
        LOG.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
        DeferredResult<ResponseEntity<SnowstormSearchResponse>> deferredResult = new DeferredResult<>(requestTimeOut);
        setCallbacks(deferredResult, CONCEPTS);
        ForkJoinPool.commonPool().submit(() -> {
            SnowstormSearchResponse snowstormSearchResponse = snowstormService.getConcepts(term, eclKey);
            if (snowstormSearchResponse == null) {
                deferredResult.setResult(ResponseEntity.noContent().build());
            }
            else {
                deferredResult.setResult(ResponseEntity.ok().body(snowstormSearchResponse));
            }
        });
        return deferredResult;
    }

    @GetMapping(value = REPORTABLE)
    public ResponseEntity<List<ManualClassificationDto>> isSnvsReportable(
            @RequestParam(value = "sctid", required = true) String sctid,
            @RequestParam(value = "pt", required = true) String pt) {
        LOG.debug("Input data -> sctid: {} , pt: {} ", sctid, pt);
        List<ManualClassificationBo> resultService = snowstormService.isSnvsReportable(sctid, pt);
        List<ManualClassificationDto> result = manualClassificationMapper.fromManualClassificationBoList(resultService);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    private <R> void setCallbacks(DeferredResult<ResponseEntity<R>> deferredResult, String serviceName) {
        deferredResult.onTimeout(() -> {
            LOG.error("TimeOut en la invocación del servicio {}", serviceName);
            deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.SC_REQUEST_TIMEOUT).body(TIMEOUT_MSG));
        });
        deferredResult.onError(e -> {
            LOG.error("Error invocando {} ", serviceName);
            deferredResult
                    .setErrorResult(ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.toString()));
        });
    }

    @GetMapping(value = "/ecl")
    public List<SnomedEclDto> getEcls() {
        return fetchAllSnomedEcl.run().stream()
                .map(snomedECLBo -> new SnomedEclDto(snomedECLBo.getKey(), snomedECLBo.getValue()))
                .collect(Collectors.toList());
    }
}
