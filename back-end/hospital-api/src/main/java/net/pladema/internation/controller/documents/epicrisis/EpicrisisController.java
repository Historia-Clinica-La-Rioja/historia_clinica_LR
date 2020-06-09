package net.pladema.internation.controller.documents.epicrisis;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.CreateUpdateEpicrisisValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.internation.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.internation.events.OnGenerateDocumentEvent;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.epicrisis.CreateEpicrisisService;
import net.pladema.internation.service.documents.epicrisis.EpicrisisService;
import net.pladema.internation.service.documents.epicrisis.UpdateEpicrisisService;
import net.pladema.internation.service.documents.epicrisis.domain.EpicrisisBo;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.internment.InternmentStateService;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
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
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis")
@Api(value = "Epicrisis", tags = { "Epicrisis" })
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
public class EpicrisisController {

    private static final Logger LOG = LoggerFactory.getLogger(EpicrisisController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEpicrisisService createEpicrisisService;

    private final UpdateEpicrisisService updateEpicrisisService;

    private final EpicrisisService epicrisisService;

    private final EpicrisisMapper epicrisisMapper;

    private final InternmentStateService internmentStateService;

    private final PdfService pdfService;

    public EpicrisisController(InternmentEpisodeService internmentEpisodeService,
                               CreateEpicrisisService createEpicrisisService,
                               UpdateEpicrisisService updateEpicrisisService,
                               EpicrisisService epicrisisService,
                               EpicrisisMapper epicrisisMapper,
                               InternmentStateService internmentStateService,
                               PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEpicrisisService = createEpicrisisService;
        this.updateEpicrisisService = updateEpicrisisService;
        this.epicrisisService = epicrisisService;
        this.epicrisisMapper = epicrisisMapper;
        this.internmentStateService = internmentStateService;
        this.pdfService = pdfService;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @CreateUpdateEpicrisisValid
    public ResponseEntity<ResponseEpicrisisDto> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EpicrisisDto epicrisisDto) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, epicrisisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        epicrisis = createEpicrisisService.createDocument(internmentEpisodeId, patientId, epicrisis);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        generateDocument(epicrisis, institutionId, internmentEpisodeId, patientId);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{epicrisisId}")
    @InternmentValid
    @CreateUpdateEpicrisisValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> updateDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId,
            @Valid @RequestBody EpicrisisDto epicrisisDto) throws IOException, DocumentException{
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, epicrisisDto {}",
                institutionId, internmentEpisodeId, epicrisisId, epicrisisDto);
        EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        epicrisis = updateEpicrisisService.updateDocument(internmentEpisodeId, patientId, epicrisis);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        generateDocument(epicrisis, institutionId, internmentEpisodeId, patientId);
        return  ResponseEntity.ok().body(result);
    }

    private void generateDocument(EpicrisisBo epicrisis, Integer institutionId, Integer internmentEpisodeId,
                                  Integer patientId) throws IOException, DocumentException {
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(epicrisis, institutionId, internmentEpisodeId,
                DocumentType.EPICRISIS, "epicrisis", patientId);
        pdfService.loadDocument(event);
    }

    @GetMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> getDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}",
                institutionId, internmentEpisodeId, epicrisisId);
        EpicrisisBo epicrisis = epicrisisService.getDocument(epicrisisId);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/general")
    @InternmentValid
    public ResponseEntity<EpicrisisGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        InternmentGeneralState interment = internmentStateService.getInternmentGeneralState(internmentEpisodeId);
        EpicrisisGeneralStateDto result = epicrisisMapper.toEpicrisisGeneralStateDto(interment);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }
}