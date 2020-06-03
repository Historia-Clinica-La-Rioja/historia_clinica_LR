package net.pladema.internation.controller.documents.evolutionnote;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.internation.controller.constraints.DocumentValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.repository.masterdata.entity.DocumentType;
import net.pladema.internation.service.documents.evolutionnote.EvolutionNoteReportService;
import net.pladema.internation.service.documents.evolutionnote.domain.EvolutionNoteBo;
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
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/evolutionNote-report")
@Api(value = "Evolution Note Report", tags = { "Evolution note Report" })
@Validated
public class EvolutionNoteReportController {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteReportController.class);

    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final EvolutionNoteReportService evolutionNoteReportService;

    private final PdfService pdfService;

    public EvolutionNoteReportController(InternmentEpisodeService internmentEpisodeService,
                                         EvolutionNoteReportService evolutionNoteReportService,
                                         PdfService pdfService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.evolutionNoteReportService = evolutionNoteReportService;
        this.pdfService = pdfService;
    }

    @GetMapping("/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EVALUATION_NOTE)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<InputStreamResource> getPDF(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId) throws IOException, DocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNoteBo evolutionNote = evolutionNoteReportService.getDocument(evolutionNoteId);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException("internmentepisode.invalid"));
        String name = "EvolutionNote_" + evolutionNote.getId();
        return pdfService.getResponseEntityPdf(name, "evolutionnote", pdfService.buildContext(evolutionNote, patientId));
    }

}