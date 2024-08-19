package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis;

import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.HospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.DeleteEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
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

@Tag(name = "Epicrisis", description = "Epicrisis")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis")
@Slf4j
@Validated
@RequiredArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
@RestController
public class EpicrisisController {

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEpicrisisService createEpicrisisService;

    private final EpicrisisService epicrisisService;

    private final EpicrisisMapper epicrisisMapper;

    private final FetchHospitalizationGeneralState fetchHospitalizationGeneralState;

    private final DeleteEpicrisisService deleteEpicrisisService;

    private final UpdateEpicrisisService updateEpicrisisService;

    private final SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;


    @PostMapping
    public ResponseEntity<Boolean> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody EpicrisisDto epicrisisDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisis {}",
                institutionId, internmentEpisodeId, epicrisisDto);
        EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        epicrisis.setEncounterId(internmentEpisodeId);
        epicrisis.setInstitutionId(institutionId);
        setPatientFromInternmentEpisode.run(epicrisis);
        epicrisis.setRoomId(internmentEpisodeService.getInternmentEpisodeRoomId(internmentEpisodeId));
        epicrisis.setSectorId(internmentEpisodeService.getInternmentEpisodeSectorId(internmentEpisodeId));
        internmentEpisodeService.getMedicalCoverage(internmentEpisodeId).ifPresent(medicalCoverage -> epicrisis.setMedicalCoverageId(medicalCoverage.getId()));
        createEpicrisisService.execute(epicrisis, false);

        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @GetMapping("/{epicrisisId}")
    @InternmentValid
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO')")
    public ResponseEntity<ResponseEpicrisisDto> getDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}",
                institutionId, internmentEpisodeId, epicrisisId);
        EpicrisisBo epicrisis = epicrisisService.getDocument(epicrisisId);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/general")
    @InternmentValid
    public ResponseEntity<EpicrisisGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        HospitalizationGeneralState interment = fetchHospitalizationGeneralState.getInternmentGeneralState(internmentEpisodeId);
        EpicrisisGeneralStateDto result = epicrisisMapper.toEpicrisisGeneralStateDto(interment);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{epicrisisId}")
    public ResponseEntity<Boolean> deleteEpicrisis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId,
            @RequestBody String reason) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, reason {}",
                institutionId, internmentEpisodeId, epicrisisId, reason);
        deleteEpicrisisService.execute(internmentEpisodeId, epicrisisId, reason);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PutMapping("/{epicrisisId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<Long> updateEpicrisis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId,
            @RequestBody EpicrisisDto epicrisisDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, newEpicrisis {}",
                institutionId, internmentEpisodeId, epicrisisId, epicrisisDto);
        EpicrisisBo newEpicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        newEpicrisis.setInstitutionId(institutionId);
        newEpicrisis.setEncounterId(internmentEpisodeId);
        setPatientFromInternmentEpisode.run(newEpicrisis);
        Long result = updateEpicrisisService.execute(internmentEpisodeId, epicrisisId, newEpicrisis);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/existUpdates")
    @InternmentValid
    public ResponseEntity<Boolean> existUpdatesAfterEpicrisis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);

        Boolean result = internmentEpisodeService.haveUpdatesAfterEpicrisis(internmentEpisodeId);

        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}