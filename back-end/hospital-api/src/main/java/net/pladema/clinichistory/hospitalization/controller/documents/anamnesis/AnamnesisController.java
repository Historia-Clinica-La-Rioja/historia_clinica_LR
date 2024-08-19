package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.mapper.AnamnesisMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.DeleteAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.UpdateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Anamnesis", description = "Anamnesis")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class AnamnesisController {

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateAnamnesisService createAnamnesisService;

    private final AnamnesisService anamnesisService;

    private final AnamnesisMapper anamnesisMapper;

    private final DeleteAnamnesisService deleteAnamnesisService;

    private final UpdateAnamnesisService updateAnamnesisService;

    private final SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<Boolean> createAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody AnamnesisDto anamnesisDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        AnamnesisBo anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis.setEncounterId(internmentEpisodeId);
        anamnesis.setInstitutionId(institutionId);
        anamnesis.setRoomId(internmentEpisodeService.getInternmentEpisodeRoomId(internmentEpisodeId));
        anamnesis.setSectorId(internmentEpisodeService.getInternmentEpisodeSectorId(internmentEpisodeId));
        internmentEpisodeService.getMedicalCoverage(internmentEpisodeId).ifPresent(medicalCoverage -> anamnesis.setMedicalCoverageId(medicalCoverage.getId()));
        setPatientFromInternmentEpisode.run(anamnesis);
        createAnamnesisService.execute(anamnesis);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @GetMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.ANAMNESIS)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<ResponseAnamnesisDto> getAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        AnamnesisBo anamnesis = anamnesisService.getDocument(anamnesisId);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{anamnesisId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<Boolean> deleteAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId,
            @RequestBody String reason) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, " +
                        "anamnesisId {}, reason {}",
                institutionId, internmentEpisodeId, anamnesisId, reason);
        deleteAnamnesisService.execute(internmentEpisodeId, anamnesisId, reason);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PutMapping("/{anamnesisId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<Long> updateAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId,
            @RequestBody AnamnesisDto anamnesisDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}, newAnanmnesis {}",
                institutionId, internmentEpisodeId, anamnesisId, anamnesisDto);
        AnamnesisBo newAnamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        newAnamnesis.setInstitutionId(institutionId);
        newAnamnesis.setEncounterId(internmentEpisodeId);
		setPatientFromInternmentEpisode.run(newAnamnesis);
        Long result = updateAnamnesisService.execute(internmentEpisodeId, anamnesisId, newAnamnesis);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

}