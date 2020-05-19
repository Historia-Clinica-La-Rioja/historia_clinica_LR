package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeADto;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.internment.dto.InternmentSummaryDto;
import net.pladema.internation.controller.internment.mapper.InternmentEpisodeMapper;
import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.repository.core.entity.InternmentEpisode;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/institutions/{institutionId}/internments")
@Api(value = "Internment Episode", tags = { "Internment Episode" })
public class InternmentEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeController.class);

    private final InternmentEpisodeService internmentEpisodeService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService,
                                       HealthcareProfessionalExternalService healthcareProfessionalExternalService, InternmentEpisodeMapper internmentEpisodeMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
    }

    @InternmentValid
    @GetMapping("/{internmentEpisodeId}/summary")
    public ResponseEntity<InternmentSummaryDto> internmentEpisodeSummary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        InternmentSummaryVo internmentSummaryVo = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
                .orElse(new InternmentSummaryVo());
        InternmentSummaryDto result = internmentEpisodeMapper.toInternmentSummaryDto(internmentSummaryVo);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<InternmentEpisodeDto> addInternmentEpisode(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody InternmentEpisodeADto internmentEpisodeADto) {
        InternmentEpisode internmentEpisodeToSave = internmentEpisodeMapper.toInternmentEpisode(internmentEpisodeADto);
        internmentEpisodeToSave = internmentEpisodeService.addInternmentEpisode(internmentEpisodeToSave, institutionId);
        InternmentEpisodeDto result = internmentEpisodeMapper.toInternmentEpisodeDto(internmentEpisodeToSave);
        healthcareProfessionalExternalService.addHealthcareProfessionalGroup(result.getId(), internmentEpisodeADto.getResponsibleDoctorId());
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }
}