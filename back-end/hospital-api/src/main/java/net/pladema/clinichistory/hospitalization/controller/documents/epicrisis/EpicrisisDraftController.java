package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CloseEpicrisisDraftService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisDraftService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Epicrisis Draft", description = "Epicrisis Draft")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis/draft")
@Slf4j
@Validated
@RequiredArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR, ESPECIALISTA_EN_ODONTOLOGIA')")
@RestController
public class EpicrisisDraftController {

    public static final String OUTPUT = "Output -> {}";

    private final CreateEpicrisisService createEpicrisisService;

    private final EpicrisisService epicrisisService;

    private final EpicrisisMapper epicrisisMapper;

    private final UpdateEpicrisisDraftService updateEpicrisisDraftService;

    private final CloseEpicrisisDraftService closeEpicrisisDraftService;

	private final SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;

    @PostMapping
    public ResponseEntity<Boolean> createDraft(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody EpicrisisDto epicrisisDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisis {}",
                institutionId, internmentEpisodeId, epicrisisDto);
        EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        epicrisis.setEncounterId(internmentEpisodeId);
        epicrisis.setInstitutionId(institutionId);
		setPatientFromInternmentEpisode.run(epicrisis);
        createEpicrisisService.execute(epicrisis, true);

        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @GetMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> getDraft(
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

    @PutMapping("/{epicrisisId}")
    public ResponseEntity<Long> updateDraft(
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
        Long result = updateEpicrisisDraftService.run(internmentEpisodeId, epicrisisId, newEpicrisis);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/final/{epicrisisId}")
    public ResponseEntity<Long> closeDraft(
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
        Long result = closeEpicrisisDraftService.execute(internmentEpisodeId, epicrisisId, newEpicrisis);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
