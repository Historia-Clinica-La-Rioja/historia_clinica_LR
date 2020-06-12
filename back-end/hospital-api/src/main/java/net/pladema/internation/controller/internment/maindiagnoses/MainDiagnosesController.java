package net.pladema.internation.controller.internment.maindiagnoses;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.internation.controller.internment.maindiagnoses.dto.MainDiagnosisDto;
import net.pladema.internation.controller.ips.mapper.HealthConditionMapper;
import net.pladema.internation.events.OnGenerateDocumentEvent;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.InternmentDocument;
import net.pladema.internation.service.documents.evolutionnote.EvolutionNoteService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.internment.maindiagnoses.MainDiagnosesService;
import net.pladema.internation.service.internment.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.sgx.pdf.PdfService;
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
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/main-diagnoses")
@Api(value = "Main diagnoses", tags = { "Main diagnoses" })
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
@Validated
public class MainDiagnosesController {

    private static final Logger LOG = LoggerFactory.getLogger(MainDiagnosesController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_INTERNMENT_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final MainDiagnosesService mainDiagnosesService;

    private final EvolutionNoteService evolutionNoteService;

    private final HealthConditionMapper healthConditionMapper;

    private final PdfService pdfService;

    public MainDiagnosesController(InternmentEpisodeService internmentEpisodeService,
                                   MainDiagnosesService mainDiagnosesService,
                                   EvolutionNoteService evolutionNoteService,
                                   HealthConditionMapper healthConditionMapper,
                                   PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.mainDiagnosesService = mainDiagnosesService;
        this.evolutionNoteService = evolutionNoteService;
        this.healthConditionMapper = healthConditionMapper;
        this.pdfService = pdfService;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @EvolutionNoteValid
    public ResponseEntity<Long> addMainDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid MainDiagnosisDto mainDiagnosis) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, mainDiagnosis {}",
                institutionId, internmentEpisodeId, mainDiagnosis);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));

        MainDiagnosisBo mainDiagnoseBo = healthConditionMapper.fromMainDiagnoseDto(mainDiagnosis);
        Long documentId = mainDiagnosesService.createDocument(internmentEpisodeId, patientId, mainDiagnoseBo);

        EvolutionNoteBo evolutionNoteResult = evolutionNoteService.getDocument(documentId);
        generateDocument(evolutionNoteResult, institutionId, internmentEpisodeId, patientId);
        Long result = evolutionNoteResult.getId();
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    private void generateDocument(InternmentDocument evolutionNote, Integer institutionId, Integer internmentEpisodeId,
                                  Integer patientId) throws IOException, DocumentException {

        LOG.debug("Input parameters -> evolutionNote {}, institutionId {}, internmentEpisodeId {} , patientId {}",
                evolutionNote, institutionId, internmentEpisodeId, patientId);
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(evolutionNote, institutionId, internmentEpisodeId,
                DocumentType.EVALUATION_NOTE, "evolutionnote", patientId);
        pdfService.loadDocument(event);
    }


}