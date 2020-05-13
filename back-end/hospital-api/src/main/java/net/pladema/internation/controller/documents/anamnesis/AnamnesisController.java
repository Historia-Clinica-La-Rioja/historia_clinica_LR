package net.pladema.internation.controller.documents.anamnesis;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.AnamnesisMainDiagnosisValid;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.anamnesis.constraints.AnamnesisValid;
import net.pladema.internation.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.internation.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import net.pladema.internation.controller.documents.anamnesis.mapper.AnamnesisMapper;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;
import net.pladema.internation.service.internment.InternmentEpisodeService;
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
    public static final String OUTPUT = "Output -> {}";
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

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
    @InternmentValid
    @AnamnesisValid
    @AnamnesisMainDiagnosisValid
    //TODO validar que diagnosticos descatados solo tengan estado REMISSION o SOLVED
    //TODO vaidar que diagnosticos ingresador por error solo tengan estado INACTIVE
    public ResponseEntity<ResponseAnamnesisDto> createAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis = createAnamnesisService.createDocument(internmentEpisodeId, patientId, anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.ANAMNESIS)
    public ResponseEntity<ResponseAnamnesisDto> updateAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId,
            @Valid @RequestBody AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisId, anamnesisDto);
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        anamnesis = updateAnamnesisService.updateDocument(internmentEpisodeId, patientId, anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.ANAMNESIS)
    public ResponseEntity<ResponseAnamnesisDto> getAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        Anamnesis anamnesis = anamnesisService.getDocument(anamnesisId);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }
}