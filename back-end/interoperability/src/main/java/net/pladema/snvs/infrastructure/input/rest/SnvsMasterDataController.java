package net.pladema.snvs.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.fetchmanualclassification.FetchManualClassification;
import net.pladema.snvs.domain.event.SnvsEventManualClassificationsBo;
import net.pladema.snvs.domain.problem.SnvsProblemBo;
import net.pladema.snvs.domain.problem.exceptions.SnvsProblemBoException;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import net.pladema.snvs.infrastructure.input.rest.dto.ManualClassificationDto;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsEventDto;
import net.pladema.snvs.infrastructure.input.rest.dto.SnvsEventManualClassificationsDto;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/snvs")
@Tag(name = "Snvs master data", description = "Snvs master data")
@Conditional(SnvsCondition.class)
@Slf4j
public class SnvsMasterDataController {

    public static final String OUTPUT = "Output -> {}";

    private final FetchManualClassification fetchManualClassification;

    public SnvsMasterDataController(FetchManualClassification fetchManualClassification){
        this.fetchManualClassification = fetchManualClassification;
    }

    @GetMapping(value = "/manual-classifications")
    @ResponseStatus(code = HttpStatus.OK)
    public List<SnvsEventManualClassificationsDto> fetchManualClassification(
            @RequestParam(value = "sctid") String sctid,
            @RequestParam(value = "pt") String pt) throws SnvsProblemBoException {
        log.debug("Input data -> sctid: {} , pt: {} ", sctid, pt);
        List<SnvsEventManualClassificationsDto> result = fetchManualClassification.run(new SnvsProblemBo(sctid, pt)).stream()
                .map(this::buildSnvsEventManualClassificationsDto)
                        .collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    private SnvsEventManualClassificationsDto buildSnvsEventManualClassificationsDto(SnvsEventManualClassificationsBo snvsEventManualClassificationsBo) {
        return SnvsEventManualClassificationsDto.builder()
                .snvsEvent(new SnvsEventDto(snvsEventManualClassificationsBo.getSnvsEventBo()))
                .manualClassifications(snvsEventManualClassificationsBo.getManualClassifications().stream()
                        .map(manualClassificationBo -> new ManualClassificationDto(manualClassificationBo.getId(),
                                manualClassificationBo.getDescription()))
                        .collect(Collectors.toList()))
                .build();
    }
}
