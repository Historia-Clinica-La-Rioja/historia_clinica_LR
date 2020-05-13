package net.pladema.internation.controller.documents.anamnesis;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.AnamnesisMainDiagnosisValid;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.documents.anamnesis.constraints.AnamnesisValid;
import net.pladema.internation.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.internation.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import net.pladema.internation.controller.documents.anamnesis.mapper.AnamnesisMapper;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleDoctorMapper;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.DocumentService;
import net.pladema.internation.service.documents.anamnesis.AnamnesisService;
import net.pladema.internation.service.documents.anamnesis.CreateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.UpdateAnamnesisService;
import net.pladema.internation.service.documents.anamnesis.domain.Anamnesis;
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
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis")
@Api(value = "Anamnesis", tags = { "Anamnesis" })
@Validated
public class AnamnesisController {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisController.class);
    public static final String OUTPUT = "Output -> {}";
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateAnamnesisService createAnamnesisService;

    private final UpdateAnamnesisService updateAnamnesisService;

    private final AnamnesisService anamnesisService;

    private final AnamnesisMapper anamnesisMapper;

    private final PatientExternalService patientExternalService;

    private final PdfService pdfService;

    private final DocumentService documentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    public AnamnesisController(InternmentEpisodeService internmentEpisodeService,
                               CreateAnamnesisService createAnamnesisService,
                               UpdateAnamnesisService updateAnamnesisService,
                               AnamnesisService anamnesisService,
                               AnamnesisMapper anamnesisMapper,
                               PatientExternalService patientExternalService,
                               PdfService pdfService,
                               DocumentService documentService,
                               ResponsibleDoctorMapper responsibleDoctorMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createAnamnesisService = createAnamnesisService;
        this.updateAnamnesisService = updateAnamnesisService;
        this.anamnesisService = anamnesisService;
        this.anamnesisMapper = anamnesisMapper;
        this.patientExternalService = patientExternalService;
        this.pdfService = pdfService;
        this.documentService = documentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
    }

    @PostMapping
    @Transactional
    @InternmentValid
    @AnamnesisValid
    @AnamnesisMainDiagnosisValid
    public ResponseEntity<ResponseAnamnesisDto> createAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis = createAnamnesisService.createDocument(internmentEpisodeId, patientId, anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.ANAMNESIS)
    public ResponseEntity<ResponseAnamnesisDto> updateAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId,
            @Valid @RequestBody AnamnesisDto anamnesisDto){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisId, anamnesisDto);
        Anamnesis anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        anamnesis = updateAnamnesisService.updateDocument(internmentEpisodeId, patientId, anamnesis);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.ANAMNESIS)
    public ResponseEntity<ResponseAnamnesisDto> getAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        Anamnesis anamnesis = anamnesisService.getDocument(anamnesisId);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{anamnesisId}/report")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.ANAMNESIS)
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        Anamnesis anamnesis = anamnesisService.getDocument(anamnesisId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        BasicPatientDto patientData = patientExternalService.getBasicDataFromPatient(patientId);
        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                documentService.getAuthor(anamnesisId));
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        Context ctx = createAnamnesisContext(result, patientData, author);
        String name = "Anamnesis_" + result.getId();
        return pdfService.getResponseEntityPdf(name, "anamnesis", LocalDateTime.now(), ctx);
    }

    private Context createAnamnesisContext(ResponseAnamnesisDto anamnesis, BasicPatientDto patientData, ResponsibleDoctorDto author ) {
        LOG.debug("Input parameters -> anamnesis {}", anamnesis);
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientData);
        ctx.setVariable("mainDiagnosis", anamnesis.getMainDiagnosis());
        ctx.setVariable("diagnosis", anamnesis.getDiagnosis());
        ctx.setVariable("personalHistories", anamnesis.getPersonalHistories());
        ctx.setVariable("familyHistories", anamnesis.getFamilyHistories());
        ctx.setVariable("allergies", anamnesis.getAllergies());
        ctx.setVariable("inmunizations", anamnesis.getInmunizations());
        ctx.setVariable("medications", anamnesis.getMedications());
        ctx.setVariable("anthropometricData", anamnesis.getAnthropometricData());
        ctx.setVariable("vitalSigns", anamnesis.getVitalSigns());
        ctx.setVariable("notes", anamnesis.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }



}