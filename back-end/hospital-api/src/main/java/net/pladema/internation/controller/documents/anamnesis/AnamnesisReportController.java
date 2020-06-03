package net.pladema.internation.controller.documents.anamnesis;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.anamnesis.AnamnesisReportService;
import net.pladema.internation.service.documents.anamnesis.domain.AnamnesisBo;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.pdf.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis-report")
@Api(value = "Anamnesis Report", tags = { "Anamnesis Report" })
@Validated
public class AnamnesisReportController {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisReportController.class);

    public static final String OUTPUT = "Output -> {}";
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final AnamnesisReportService anamnesisReportService;

    private final PdfService pdfService;

    public AnamnesisReportController(InternmentEpisodeService internmentEpisodeService,
                                     AnamnesisReportService anamnesisReportService,
                                     PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.anamnesisReportService = anamnesisReportService;
        this.pdfService = pdfService;
    }


    @GetMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.ANAMNESIS)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        AnamnesisBo anamnesis = anamnesisReportService.getDocument(anamnesisId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_EPISODE));
        String name = "Anamnesis_" + anamnesis.getId();
        return pdfService.getResponseEntityPdf(name, "anamnesis", pdfService.buildContext(anamnesis, patientId));
    }

}