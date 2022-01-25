package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.pdf.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/institutions/{institutionId}/documents")
@Slf4j
//@Api(value = "Documents", tags = {"Documents"})
public class DocumentController {

    private final FetchDocumentFileById fetchDocumentFileById;

    private final PdfService pdfService;

    private final FeatureFlagsService featureFlagsService;

    public DocumentController(FetchDocumentFileById fetchDocumentFileById, PdfService pdfService,
                              FeatureFlagsService featureFlagsService){
        this.fetchDocumentFileById = fetchDocumentFileById;
        this.pdfService = pdfService;
        this.featureFlagsService = featureFlagsService;
    }

    @GetMapping(value = "/{id}/downloadFile")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable(name = "institutionId") Integer institutionId,
                                                           @PathVariable(name = "id") Long id) {
        var documentFile = fetchDocumentFileById.run(id);
        ByteArrayInputStream pdfFile;
        long sizeFile;
        try {
            pdfFile = pdfService.reader(documentFile.getFilepath());
            sizeFile = Files.size(Paths.get(documentFile.getFilepath()));
        } catch (PDFDocumentException | IOException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        InputStreamResource resource = new InputStreamResource(pdfFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + documentFile.getFilename())
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(sizeFile)
                .body(resource);
    }
}
