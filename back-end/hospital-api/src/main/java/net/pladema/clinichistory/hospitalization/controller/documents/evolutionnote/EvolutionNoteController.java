package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis.EvolutionDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper.EvolutionNoteMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteReportService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.sgx.pdf.PDFDocumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/evolutionNote")
@Api(value = "Evolution Note", tags = { "Evolution note" })
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
public class EvolutionNoteController {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_INTERNMENT_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEvolutionNoteService createEvolutionNoteService;

    private final EvolutionNoteService evolutionNoteService;

    private final EvolutionDiagnosesService evolutionDiagnosesService;

    private final EvolutionNoteReportService evolutionNoteReportService;

    private final EvolutionNoteMapper evolutionNoteMapper;

    private final PatientExternalService patientExternalService;

    public EvolutionNoteController(InternmentEpisodeService internmentEpisodeService,
                                   CreateEvolutionNoteService createEvolutionNoteService,
                                   EvolutionNoteService evolutionNoteService,
                                   EvolutionDiagnosesService evolutionDiagnosesService,
                                   EvolutionNoteReportService evolutionNoteReportService,
                                   EvolutionNoteMapper evolutionNoteMapper,
                                   PatientExternalService patientExternalService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEvolutionNoteService = createEvolutionNoteService;
        this.evolutionNoteService = evolutionNoteService;
        this.evolutionDiagnosesService = evolutionDiagnosesService;
        this.evolutionNoteReportService = evolutionNoteReportService;
        this.evolutionNoteMapper = evolutionNoteMapper;
        this.patientExternalService = patientExternalService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Boolean> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody EvolutionNoteDto evolutionNoteDto) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteDto);
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        internmentEpisodeService.getPatient(internmentEpisodeId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(evolutionNote::setPatientInfo,() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
        evolutionNote.setEncounterId(internmentEpisodeId);
        evolutionNote.setInstitutionId(institutionId);
        createEvolutionNoteService.execute(evolutionNote);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/evolutionDiagnosis")
    @Transactional
    @InternmentValid
    @EvolutionNoteValid
    public ResponseEntity<Boolean> createEvolutionDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionDiagnosisDto evolutionDiagnosisDto) {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionDiagnosisDto {}",
                institutionId, internmentEpisodeId, evolutionDiagnosisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));

        EvolutionDiagnosisBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionDiagnosisDto);
        Long documentId = evolutionDiagnosesService.execute(internmentEpisodeId, patientId, evolutionNote);

        evolutionNoteReportService.getDocument(documentId);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }



    @GetMapping("/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<ResponseEvolutionNoteDto> getDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNoteBo evolutionNoteBo = evolutionNoteService.getDocument(evolutionNoteId);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNoteBo);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }
}