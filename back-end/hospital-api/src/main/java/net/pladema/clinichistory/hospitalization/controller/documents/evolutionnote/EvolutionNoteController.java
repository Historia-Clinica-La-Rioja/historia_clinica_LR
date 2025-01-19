package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis.EvolutionDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper.EvolutionNoteMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.DeleteEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
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


@Tag(name = "Evolution notes", description = "Evolution notes")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/evolutionNote")
@Slf4j
@Validated
@RequiredArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
@RestController
public class EvolutionNoteController {

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEvolutionNoteService createEvolutionNoteService;

    private final EvolutionNoteService evolutionNoteService;

    private final EvolutionDiagnosesService evolutionDiagnosesService;

    private final EvolutionNoteMapper evolutionNoteMapper;

    private final DeleteEvolutionNoteService deleteEvolutionNoteService;

    private final UpdateEvolutionNoteService updateEvolutionNoteService;

    private final SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;

    @PostMapping
    public ResponseEntity<Boolean> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody EvolutionNoteDto evolutionNoteDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteDto);
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        internmentEpisodeService.getMedicalCoverage(internmentEpisodeId).ifPresent(medicalCoverage -> evolutionNote.setMedicalCoverageId(medicalCoverage.getId()));
        evolutionNote.setRoomId(internmentEpisodeService.getInternmentEpisodeRoomId(internmentEpisodeId));
        evolutionNote.setSectorId(internmentEpisodeService.getInternmentEpisodeSectorId(internmentEpisodeId));
        evolutionNote.setEncounterId(internmentEpisodeId);
        evolutionNote.setInstitutionId(institutionId);
        setPatientFromInternmentEpisode.run(evolutionNote);
        createEvolutionNoteService.execute(evolutionNote);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/evolutionDiagnosis")
    @InternmentValid
    @EvolutionNoteValid
    public ResponseEntity<Boolean> createEvolutionDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionDiagnosisDto evolutionDiagnosisDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionDiagnosisDto {}",
                institutionId, internmentEpisodeId, evolutionDiagnosisDto);
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionDiagnosisDto(evolutionDiagnosisDto);
        evolutionNote.setInstitutionId(institutionId);
        evolutionNote.setEncounterId(internmentEpisodeId);
        evolutionNote.setIsNursingEvolutionNote(false);
        setPatientFromInternmentEpisode.run(evolutionNote);
        evolutionDiagnosesService.execute(evolutionNote);

        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @GetMapping("/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<ResponseEvolutionNoteDto> getDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNoteBo evolutionNoteBo = evolutionNoteService.getDocument(evolutionNoteId);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNoteBo);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/nursing/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.NURSING_EVOLUTION_NOTE)
    public ResponseEntity<ResponseEvolutionNoteDto> getNursingDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNoteBo evolutionNoteBo = evolutionNoteService.getDocument(evolutionNoteId);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNoteBo);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }


    @DeleteMapping("/{evolutionNoteId}")
    public ResponseEntity<Boolean> deleteEvolutionNote(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId,
            @RequestBody String reason) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, " +
                        "evolutionNoteId {}, reason {}",
                institutionId, internmentEpisodeId, evolutionNoteId, reason);
        deleteEvolutionNoteService.execute(internmentEpisodeId, evolutionNoteId, reason);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PutMapping("/{evolutionNoteId}")
    public ResponseEntity<Long> updateAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId,
            @RequestBody @Valid EvolutionNoteDto evolutionNoteDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}, evolutionNoteDto {}",
                institutionId, internmentEpisodeId, evolutionNoteId, evolutionNoteDto);
        EvolutionNoteBo newEvolution = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        newEvolution.setInstitutionId(institutionId);
        newEvolution.setEncounterId(internmentEpisodeId);
        setPatientFromInternmentEpisode.run(newEvolution);
        Long result = updateEvolutionNoteService.execute(internmentEpisodeId, evolutionNoteId, newEvolution);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

}