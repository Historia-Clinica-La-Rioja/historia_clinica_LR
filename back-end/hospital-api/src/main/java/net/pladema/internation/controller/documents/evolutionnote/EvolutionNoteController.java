package net.pladema.internation.controller.documents.evolutionnote;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.EvolutionNoteDiagnosisValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.internation.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.internation.controller.documents.evolutionnote.mapper.EvolutionNoteMapper;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleDoctorMapper;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.evolutionnote.CreateEvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.EvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNote;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.pdf.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/evolutionNote")
@Api(value = "Evolution Note", tags = { "Evolution note" })
@Validated
public class EvolutionNoteController {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteController.class);

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEvolutionNoteService createEvolutionNoteService;

    private final UpdateEvolutionNoteService updateEvolutionNoteService;

    private final EvolutionNoteService evolutionNoteService;

    private final EvolutionNoteMapper evolutionNoteMapper;

    private final PatientExternalService patientExternalService;

    private final PdfService pdfService;

    private final DocumentService documentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    public EvolutionNoteController(InternmentEpisodeService internmentEpisodeService,
                                   CreateEvolutionNoteService createEvolutionNoteService,
                                   UpdateEvolutionNoteService updateEvolutionNoteService,
                                   EvolutionNoteService evolutionNoteService,
                                   EvolutionNoteMapper evolutionNoteMapper,
                                   PatientExternalService patientExternalService,
                                   PdfService pdfService,
                                   DocumentService documentService,
                                   ResponsibleDoctorMapper responsibleDoctorMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEvolutionNoteService = createEvolutionNoteService;
        this.updateEvolutionNoteService = updateEvolutionNoteService;
        this.evolutionNoteService = evolutionNoteService;
        this.evolutionNoteMapper = evolutionNoteMapper;
        this.patientExternalService = patientExternalService;
        this.pdfService = pdfService;
        this.documentService = documentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @EvolutionNoteDiagnosisValid
    //TODO validar que diagnosticos descatados solo tengan estado REMISSION o SOLVED
    //TODO vaidar que diagnosticos ingresador por error solo tengan estado INACTIVE
    public ResponseEntity<ResponseEvolutionNoteDto> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionNoteDto evolutionNoteDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        EvolutionNote evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        evolutionNote = createEvolutionNoteService.createDocument(internmentEpisodeId, patientId, evolutionNote);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNote);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<ResponseEvolutionNoteDto> updateDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId,
            @Valid @RequestBody EvolutionNoteDto evolutionNoteDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteId, evolutionNoteDto);
        EvolutionNote evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        evolutionNote = updateEvolutionNoteService.updateDocument(internmentEpisodeId, patientId, evolutionNote);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNote);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
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
        EvolutionNote evolutionNote = evolutionNoteService.getDocument(evolutionNoteId);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNote);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{evolutionNoteId}/report")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNote evolutionNote = evolutionNoteService.getDocument(evolutionNoteId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        BasicPatientDto patientData = patientExternalService.getBasicDataFromPatient(patientId);
        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                documentService.getAuthor(evolutionNoteId));
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNote);
        Context ctx = createEvolutionNoteContext(result, patientData, author);
        String name = "EvolutionNote_" + result.getId();
        return pdfService.getResponseEntityPdf(name, "evolutionnote", LocalDateTime.now(), ctx);
    }

    private Context createEvolutionNoteContext(ResponseEvolutionNoteDto evolutionNoteDto, BasicPatientDto patientData, ResponsibleDoctorDto author  ) {
        LOG.debug("Input parameters -> anamnesis {}", evolutionNoteDto);
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientData);
        ctx.setVariable("diagnosis", evolutionNoteDto.getDiagnosis());
        ctx.setVariable("allergies", evolutionNoteDto.getAllergies());
        ctx.setVariable("anthropometricData", evolutionNoteDto.getAnthropometricData());
        ctx.setVariable("vitalSigns", evolutionNoteDto.getVitalSigns());
        ctx.setVariable("notes", evolutionNoteDto.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }

}