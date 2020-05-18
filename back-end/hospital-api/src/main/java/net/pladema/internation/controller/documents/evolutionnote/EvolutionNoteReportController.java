package net.pladema.internation.controller.documents.evolutionnote;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleDoctorMapper;
import net.pladema.internation.controller.ips.dto.VitalSignsReportDto;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.ReportDocumentService;
import net.pladema.internation.service.documents.evolutionnote.EvolutionNoteReportService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNote;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.pdf.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/evolutionNote-report")
@Api(value = "Evolution Note Report", tags = { "Evolution note Report" })
@Validated
public class EvolutionNoteReportController {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteReportController.class);

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final EvolutionNoteReportService evolutionNoteReportService;

    private final PatientExternalService patientExternalService;

    private final PdfService pdfService;

    private final ReportDocumentService reportDocumentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    private final VitalSignMapper vitalSignMapper;

    public EvolutionNoteReportController(InternmentEpisodeService internmentEpisodeService,
                                         EvolutionNoteReportService evolutionNoteReportService,
                                         PatientExternalService patientExternalService,
                                         PdfService pdfService,
                                         ReportDocumentService reportDocumentService,
                                         ResponsibleDoctorMapper responsibleDoctorMapper,
                                         VitalSignMapper vitalSignMapper) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.evolutionNoteReportService = evolutionNoteReportService;
        this.patientExternalService = patientExternalService;
        this.pdfService = pdfService;
        this.reportDocumentService = reportDocumentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
        this.vitalSignMapper = vitalSignMapper;
    }

    @GetMapping("/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNote evolutionNote = evolutionNoteReportService.getDocument(evolutionNoteId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        BasicPatientDto patientData = patientExternalService.getBasicDataFromPatient(patientId);
        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                reportDocumentService.getAuthor(evolutionNoteId));
        Context ctx = createEvolutionNoteContext(evolutionNote, patientData, author);
        String name = "EvolutionNote_" + evolutionNote.getId();
        return pdfService.getResponseEntityPdf(name, "evolutionnote", LocalDateTime.now(), ctx);
    }

    private Context createEvolutionNoteContext(EvolutionNote evolutionNote, BasicPatientDto patientData,
                                               ResponsibleDoctorDto author) {
        LOG.debug("Input parameters -> evolutionNote {}", evolutionNote);
        VitalSignsReportDto vitalSignsReportDto = vitalSignMapper.toVitalSignsReportDto(evolutionNote.getVitalSigns());
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientData);
        ctx.setVariable("diagnosis", evolutionNote.getDiagnosis());
        ctx.setVariable("allergies", evolutionNote.getAllergies());
        ctx.setVariable("inmunizations", evolutionNote.getInmunizations());
        ctx.setVariable("anthropometricData", evolutionNote.getAnthropometricData());
        ctx.setVariable("vitalSigns", vitalSignsReportDto);
        ctx.setVariable("notes", evolutionNote.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }

}