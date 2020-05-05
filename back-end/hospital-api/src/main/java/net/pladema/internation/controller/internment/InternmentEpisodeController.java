package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.internment.dto.InternmentSummaryDto;
import net.pladema.internation.controller.internment.mapper.InternmentEpisodeMapper;
import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/institutions/{institutionId}/internments")
@Api(value = "Internment Episode", tags = { "Internment Episode" })
public class InternmentEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeController.class);

    private final InternmentEpisodeService internmentEpisodeService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService,
                                       InternmentEpisodeMapper internmentEpisodeMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
    }

    @GetMapping("/{internmentEpisodeId}/summary")
    public ResponseEntity<InternmentSummaryDto> internmentEpisodeSummary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        InternmentSummary internmentSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
                .orElse(new InternmentSummary());
        InternmentSummaryDto result = internmentEpisodeMapper.toInternmentSummaryDto(internmentSummary);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}