package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.EvolutionNoteDiagnosisValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis.EvolutionDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper.EvolutionNoteMapper;
import net.pladema.clinichistory.hospitalization.controller.generalstate.constraint.EffectiveVitalSignTimeValid;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteReportService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.EDocumentType;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.pdf.service.PdfService;
import net.pladema.sgx.featureflags.AppFeature;
import org.apache.http.MethodNotSupportedException;
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

    private final UpdateEvolutionNoteService updateEvolutionNoteService;

    private final EvolutionNoteService evolutionNoteService;

    private final EvolutionNoteReportService evolutionNoteReportService;

    private final EvolutionNoteMapper evolutionNoteMapper;

    private final PdfService pdfService;

    private final FeatureFlagsService featureFlagsService;

    public EvolutionNoteController(InternmentEpisodeService internmentEpisodeService,
                                   CreateEvolutionNoteService createEvolutionNoteService,
                                   UpdateEvolutionNoteService updateEvolutionNoteService,
                                   EvolutionNoteService evolutionNoteService,
                                   EvolutionNoteReportService evolutionNoteReportService,
                                   EvolutionNoteMapper evolutionNoteMapper,
                                   PdfService pdfService, FeatureFlagsService featureFlagsService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEvolutionNoteService = createEvolutionNoteService;
        this.updateEvolutionNoteService = updateEvolutionNoteService;
        this.evolutionNoteService = evolutionNoteService;
        this.evolutionNoteReportService = evolutionNoteReportService;
        this.evolutionNoteMapper = evolutionNoteMapper;
        this.pdfService = pdfService;
        this.featureFlagsService = featureFlagsService;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @EvolutionNoteDiagnosisValid
    @EffectiveVitalSignTimeValid
    @EvolutionNoteValid
    public ResponseEntity<Boolean> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionNoteDto evolutionNoteDto) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        evolutionNote = createEvolutionNoteService.createDocument(internmentEpisodeId, patientId, evolutionNote);
        generateDocument(evolutionNote, institutionId, internmentEpisodeId, patientId);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PutMapping("/{evolutionNoteId}")
    @InternmentValid
    @EvolutionNoteDiagnosisValid
    @EffectiveVitalSignTimeValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<ResponseEvolutionNoteDto> updateDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId,
            @Valid @RequestBody EvolutionNoteDto evolutionNoteDto) throws IOException, DocumentException, MethodNotSupportedException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteId, evolutionNoteDto);
        if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_UPDATE_DOCUMENTS))
            throw new MethodNotSupportedException("Funcionalidad no soportada por el momento");

        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));
        evolutionNote = updateEvolutionNoteService.updateDocument(internmentEpisodeId, patientId, evolutionNote);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNote);
        LOG.debug(OUTPUT, result);
        generateDocument(evolutionNote, institutionId, internmentEpisodeId, patientId);
        return  ResponseEntity.ok().body(result);
    }

    @PostMapping("/evolutionDiagnosis")
    @Transactional
    @InternmentValid
    @EvolutionNoteValid
    public ResponseEntity<Long> createEvolutionDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionDiagnosisDto evolutionDiagnosisDto) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionDiagnosisDto {}",
                institutionId, internmentEpisodeId, evolutionDiagnosisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));

        EvolutionDiagnosisBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionDiagnosisDto);
        Long documentId = createEvolutionNoteService.createEvolutionDiagnosis(internmentEpisodeId, patientId, evolutionNote);

        EvolutionNoteBo evolutionNoteResult = evolutionNoteReportService.getDocument(documentId);
        generateDocument(evolutionNoteResult, institutionId, internmentEpisodeId, patientId);
        Long result = evolutionNoteResult.getId();
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    private void generateDocument(Document evolutionNote, Integer institutionId, Integer internmentEpisodeId,
                                  Integer patientId) throws IOException, DocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(evolutionNote, institutionId, internmentEpisodeId,
                EDocumentType.map(DocumentType.EVALUATION_NOTE), patientId);
        pdfService.loadDocument(event);
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