package net.pladema.internation.controller.documents.epicrisis;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.internation.controller.documents.epicrisis.dto.NewEpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.internation.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleDoctorMapper;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.epicrisis.CreateEpicrisisService;
import net.pladema.internation.service.documents.epicrisis.EpicrisisService;
import net.pladema.internation.service.documents.epicrisis.UpdateEpicrisisService;
import net.pladema.internation.service.documents.epicrisis.domain.Epicrisis;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.internment.InternmentStateService;
import net.pladema.internation.service.internment.domain.InternmentGeneralState;
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
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis")
@Api(value = "Epicrisis", tags = { "Epicrisis" })
@Validated
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

    private final PatientExternalService patientExternalService;

    private final PdfService pdfService;

    private final DocumentService documentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    public EpicrisisController(InternmentEpisodeService internmentEpisodeService,
                               CreateEpicrisisService createEpicrisisService,
                               UpdateEpicrisisService updateEpicrisisService,
                               EpicrisisService epicrisisService,
                               EpicrisisMapper epicrisisMapper,
                               InternmentStateService internmentStateService,
                               PatientExternalService patientExternalService,
                               PdfService pdfService,
                               DocumentService documentService,
                               ResponsibleDoctorMapper responsibleDoctorMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEpicrisisService = createEpicrisisService;
        this.updateEpicrisisService = updateEpicrisisService;
        this.epicrisisService = epicrisisService;
        this.epicrisisMapper = epicrisisMapper;
        this.internmentStateService = internmentStateService;
        this.patientExternalService = patientExternalService;
        this.pdfService = pdfService;
        this.documentService = documentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    public ResponseEntity<ResponseEpicrisisDto> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid NewEpicrisisDto epicrisisDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, epicrisisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        Epicrisis epicrisis = epicrisisMapper.fromNewEpicrisisDto(epicrisisDto);
        epicrisis = createEpicrisisService.createDocument(internmentEpisodeId, patientId, epicrisis);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> updateDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId,
            @Valid @RequestBody EpicrisisDto epicrisisDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, epicrisisDto {}",
                institutionId, internmentEpisodeId, epicrisisId, epicrisisDto);
        Epicrisis epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        epicrisis = updateEpicrisisService.updateDocument(internmentEpisodeId, patientId, epicrisis);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
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
        Epicrisis epicrisis = epicrisisService.getDocument(epicrisisId);
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

    @GetMapping("/{epicrisisId}/report")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId) throws IOException, DocumentException {

        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}",
                institutionId, internmentEpisodeId, epicrisisId);

        Epicrisis epicrisis = epicrisisService.getDocument(epicrisisId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        BasicPatientDto patientData = patientExternalService.getBasicDataFromPatient(patientId);
        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                documentService.getAuthor(epicrisisId));
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        Context ctx = createEpicrisisContext(result, patientData, author);
        String name = "Epicrisis_" + result.getId() ;
        return pdfService.getResponseEntityPdf(name, "epicrisis", LocalDateTime.now(), ctx);
    }

    private Context createEpicrisisContext(ResponseEpicrisisDto epicrisis, BasicPatientDto patientData, ResponsibleDoctorDto author ) {
        LOG.debug("Input parameters -> epicrisis {}", epicrisis);
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientData);
        ctx.setVariable("mainDiagnosis", epicrisis.getMainDiagnosis());
        ctx.setVariable("diagnosis", epicrisis.getDiagnosis());
        ctx.setVariable("personalHistories", epicrisis.getPersonalHistories());
        ctx.setVariable("familyHistories", epicrisis.getFamilyHistories());
        ctx.setVariable("allergies", epicrisis.getAllergies());
        ctx.setVariable("medications", epicrisis.getMedications());
        ctx.setVariable("notes", epicrisis.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }

}