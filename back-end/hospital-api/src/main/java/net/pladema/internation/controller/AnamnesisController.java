package net.pladema.internation.controller;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.dto.core.AnamnesisDto;
import net.pladema.internation.controller.dto.core.ResponseAnamnesisDto;
import net.pladema.internation.controller.mapper.AnamnesisMapper;
import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import net.pladema.internation.service.domain.Anamnesis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis")
@Api(value = "Anamnesis", tags = { "Anamnesis" })
public class AnamnesisController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final CreateAnamnesisService createAnamnesisService;

    private final UpdateAnamnesisService updateAnamnesisService;

    private final AnamnesisService anamnesisService;

    private final AnamnesisMapper anamnesisMapper;

    public AnamnesisController(CreateAnamnesisService createAnamnesisService, UpdateAnamnesisService updateAnamnesisService,
                               AnamnesisService anamnesisService,
                               AnamnesisMapper anamnesisMapper) {
        this.createAnamnesisService = createAnamnesisService;
        this.updateAnamnesisService = updateAnamnesisService;
        this.anamnesisService = anamnesisService;
        this.anamnesisMapper = anamnesisMapper;
    }

    @PostMapping
    public ResponseEntity<ResponseAnamnesisDto> createAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> instituionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis = createAnamnesisService.createAnanmesisDocument(anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{anamnesisId}")
    //TODO validar que este en estado borrador y que exista la anamnesis
    public ResponseEntity<ResponseAnamnesisDto> updateAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Integer anamnesisId,
            @RequestBody AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> instituionId {}, internmentEpisodeId {}, anamnesisId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);

        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis.setAnamnesisId(anamnesisId);
        anamnesis = updateAnamnesisService.updateAnanmesisDocument(anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{anamnesisId}")
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