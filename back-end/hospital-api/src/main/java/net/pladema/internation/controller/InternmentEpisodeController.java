package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.InternmentSummaryDto;
import net.pladema.internation.controller.mapper.InternmentEpisodeMapper;
import net.pladema.internation.controller.mocks.MocksInternmentPatient;
import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.service.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments")
@Api(value = "Internment Episode", tags = { "Internment Episode" })
public class InternmentEpisodeController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final InternmentEpisodeService internmentEpisodeService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService, InternmentEpisodeMapper internmentEpisodeMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
    }

    @GetMapping("/{internmentEpisodeId}/summary")
    public ResponseEntity<InternmentSummaryDto> internmentEpisodeSummary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        InternmentSummaryDto result;
        try {
            InternmentSummary internmentSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
                    .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
            result = internmentEpisodeMapper.toInternmentSummaryDto(internmentSummary);
        } catch (Exception e) {
            result = MocksInternmentPatient.mockInternmentSummary(internmentEpisodeId);
        }
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}