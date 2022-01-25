package net.pladema.establishment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.establishment.controller.dto.VInstitutionDto;
import net.pladema.establishment.controller.mapper.VInstitutionMapper;
import net.pladema.establishment.service.InstitutionGeneralStateService;
import net.pladema.establishment.service.domain.VInstitutionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Institution general state", description = "Institution general state")
@RequestMapping("/institution/{institutionId}/generalState")
public class InstitutionGeneralStateController {

    private static final Logger LOG = LoggerFactory.getLogger(InstitutionGeneralStateController.class);

    private final InstitutionGeneralStateService institutionGeneralStateService;

    private final VInstitutionMapper vInstitutionMapper;

    public InstitutionGeneralStateController(InstitutionGeneralStateService institutionGeneralStateService,
                                             VInstitutionMapper vInstitutionMapper){
        this.institutionGeneralStateService = institutionGeneralStateService;
        this.vInstitutionMapper = vInstitutionMapper;
    }

    @GetMapping
    public ResponseEntity<VInstitutionDto> getGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId) {
        LOG.debug("Input parameters -> {}", institutionId);
        VInstitutionBo generalState = institutionGeneralStateService.get(institutionId);
        VInstitutionDto result = vInstitutionMapper.toVInstitutionDto(generalState);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

}


