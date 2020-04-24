package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.core.AnamnesisDto;
import net.pladema.internation.controller.dto.core.ResponseAnamnesisDto;
import net.pladema.internation.controller.mapper.AnamnesisMapper;
import net.pladema.internation.service.InternmentEpisodeService;
import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis")
@Api(value = "Anamnesis", tags = { "Anamnesis" })
@Validated
public class AnamnesisController {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisController.class);

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateAnamnesisService createAnamnesisService;

    private final UpdateAnamnesisService updateAnamnesisService;

    private final AnamnesisService anamnesisService;

    private final AnamnesisMapper anamnesisMapper;

    public AnamnesisController(InternmentEpisodeService internmentEpisodeService,
                               CreateAnamnesisService createAnamnesisService,
                               UpdateAnamnesisService updateAnamnesisService,
                               AnamnesisService anamnesisService,
                               AnamnesisMapper anamnesisMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createAnamnesisService = createAnamnesisService;
        this.updateAnamnesisService = updateAnamnesisService;
        this.anamnesisService = anamnesisService;
        this.anamnesisMapper = anamnesisMapper;
    }

    @PostMapping
    @Transactional
    //TODO validar que exista la internación institución
    public ResponseEntity<ResponseAnamnesisDto> createAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @Valid @RequestBody AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> instituionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        Integer patientId;
        try {
            patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                    .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        } catch (Exception e) {
            patientId = internmentEpisodeId;
        }
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis = createAnamnesisService.createAnanmesisDocument(internmentEpisodeId, patientId, anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{anamnesisId}")
    //TODO validar que exista la internación institución
    //TODO validar que este en estado borrador y que exista la anamnesis
    public ResponseEntity<ResponseAnamnesisDto> updateAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId,
            @Valid @RequestBody AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> instituionId {}, internmentEpisodeId {}, anamnesisId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis.setId(anamnesisId);
        anamnesis = updateAnamnesisService.updateAnanmesisDocument(internmentEpisodeId, patientId, anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{anamnesisId}")
    //TODO validar que exista la internación institución
    //TODO validar que exista la anamnesis
    public ResponseEntity<ResponseAnamnesisDto> getAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Integer anamnesisId){
        LOG.debug("Input parameters -> instituionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        Anamnesis anamnesis = anamnesisService.getAnamnesis(anamnesisId);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

}