package net.pladema.internation.controller.documents.evolutionnote;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.EvolutionNoteDiagnosisValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.internation.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.internation.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.internation.controller.documents.evolutionnote.dto.evolutionDiagnosis.EvolutionDiagnosisDto;
import net.pladema.internation.controller.documents.evolutionnote.mapper.EvolutionNoteMapper;
import net.pladema.internation.controller.ips.constraints.EffectiveVitalSignTimeValid;
import net.pladema.internation.events.OnGenerateDocumentEvent;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.InternmentDocument;
import net.pladema.internation.service.documents.evolutionnote.CreateEvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.EvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.internation.service.documents.evolutionnote.domain.evolutiondiagnosis.EvolutionDiagnosisBo;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.pdf.PdfService;
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
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR')")
public class EvolutionNoteController {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_INTERNMENT_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEvolutionNoteService createEvolutionNoteService;

    private final UpdateEvolutionNoteService updateEvolutionNoteService;

    private final EvolutionNoteService evolutionNoteService;

    private final EvolutionNoteMapper evolutionNoteMapper;

    private final PdfService pdfService;

    public EvolutionNoteController(InternmentEpisodeService internmentEpisodeService,
                                   CreateEvolutionNoteService createEvolutionNoteService,
                                   UpdateEvolutionNoteService updateEvolutionNoteService,
                                   EvolutionNoteService evolutionNoteService,
                                   EvolutionNoteMapper evolutionNoteMapper,
                                   PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEvolutionNoteService = createEvolutionNoteService;
        this.updateEvolutionNoteService = updateEvolutionNoteService;
        this.evolutionNoteService = evolutionNoteService;
        this.evolutionNoteMapper = evolutionNoteMapper;
        this.pdfService = pdfService;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @EvolutionNoteDiagnosisValid
    @EffectiveVitalSignTimeValid
    @EvolutionNoteValid
    //TODO validar que diagnosticos descatados solo tengan estado REMISSION o SOLVED
    //TODO vaidar que diagnosticos ingresador por error solo tengan estado INACTIVE
    public ResponseEntity<ResponseEvolutionNoteDto> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionNoteDto evolutionNoteDto) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        evolutionNote = createEvolutionNoteService.createDocument(internmentEpisodeId, patientId, evolutionNote);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNote);
        LOG.debug(OUTPUT, result);
        generateDocument(evolutionNote, institutionId, internmentEpisodeId, patientId);
        return  ResponseEntity.ok().body(result);
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
            @Valid @RequestBody EvolutionNoteDto evolutionNoteDto) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteId, evolutionNoteDto);
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
    //TODO validar que diagnosticos descatados solo tengan estado REMISSION o SOLVED
    //TODO vaidar que diagnosticos ingresador por error solo tengan estado INACTIVE
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

        EvolutionNoteBo evolutionNoteResult = evolutionNoteService.getDocument(documentId);
        generateDocument(evolutionNoteResult, institutionId, internmentEpisodeId, patientId);
        Long result = evolutionNoteResult.getId();
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    private void generateDocument(InternmentDocument evolutionNote, Integer institutionId, Integer internmentEpisodeId,
                                                                   Integer patientId) throws IOException, DocumentException {
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(evolutionNote, institutionId, internmentEpisodeId,
                DocumentType.EVALUATION_NOTE, "evolutionnote", patientId);
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