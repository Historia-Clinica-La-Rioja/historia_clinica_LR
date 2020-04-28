package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.core.InternmentGeneralStateDto;
import net.pladema.internation.service.InternmentStateService;
import net.pladema.internation.service.domain.InternmentGeneralState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/institutions/{institutionId}/internments-state")
@Api(value = "Internment State", tags = { "Internment State" })
public class InternmentStateController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentStateController.class);

    private final InternmentStateService internmentStateService;

    public InternmentStateController(InternmentStateService internmentEpisodeService) {
        this.internmentStateService = internmentEpisodeService;
    }

    @GetMapping("/{internmentEpisodeId}/general")
    public ResponseEntity<InternmentGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        InternmentGeneralState interment = internmentStateService.getInternmentGeneralState(internmentEpisodeId);
        InternmentGeneralStateDto result = new InternmentGeneralStateDto();
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}