package net.pladema.snowstorm.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import net.pladema.snowstorm.services.SnowstormService;
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

import java.util.concurrent.ForkJoinPool;

@RequiredArgsConstructor
@RestController
@RequestMapping("/snowstorm")
@Api(value = "Snowstorm", tags = { "Snowstorm" })
public class SnowstormController {

    private static final String CONCEPTS = "/concepts";

    private static final String REFSET_MEMBERS = "/refset-members";

    private static final Logger LOG = LoggerFactory.getLogger(SnowstormController.class);

    private static final String TIMEOUT_MSG = "Timeout en WS Snowstorm";

    private final SnowstormService snowstormService;

    @Value("${ws.snowstorm.request.timeout:10000}")
    private long requestTimeOut;

    @GetMapping(value = CONCEPTS)
    public DeferredResult<ResponseEntity<SnowstormSearchResponse>> getConcepts(
            @RequestParam(value = "term", required = true) String term,
            @RequestParam(value = "ecl", required = false) String ecl) {
        LOG.debug("Input data -> term: {} , ecl: {} ", term, ecl);
        DeferredResult<ResponseEntity<SnowstormSearchResponse>> deferredResult = new DeferredResult<>(requestTimeOut);
        setCallbacks(deferredResult, CONCEPTS);
        ForkJoinPool.commonPool().submit(() -> {
            SnowstormSearchResponse snowstormSearchResponse = snowstormService.getConcepts(term, ecl);
            if (snowstormSearchResponse == null) {
                deferredResult.setResult(ResponseEntity.noContent().build());
            }
            else {
                deferredResult.setResult(ResponseEntity.ok().body(snowstormSearchResponse));
            }
        });
        return deferredResult;
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
}
