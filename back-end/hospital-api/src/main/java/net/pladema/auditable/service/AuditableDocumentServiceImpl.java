package net.pladema.auditable.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import net.pladema.sgx.files.StreamFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.pladema.clinichistory.documents.events.OnGenerateDocumentEvent;
import net.pladema.clinichistory.documents.repository.entity.DocumentFile;
import net.pladema.sgx.pdf.PDFDocumentException;
import net.pladema.sgx.pdf.PdfService;

@Component
public class AuditableDocumentServiceImpl implements AuditableService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditableDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final StreamFile streamFile;

    private final PdfService pdfService;

    private final AuditableContextBuilder auditableContextBuilder;

    public AuditableDocumentServiceImpl(
            StreamFile streamFile,
            PdfService pdfService,
            AuditableContextBuilder auditableContextBuilder
    ) {
        super();
        this.streamFile = streamFile;
        this.pdfService = pdfService;
        this.auditableContextBuilder = auditableContextBuilder;
    }

    @Override
    public Optional<DocumentFile> save(OnGenerateDocumentEvent event)  {
        Map<String,Object> contextMap = auditableContextBuilder.buildContext(event.getDocument(), event.getPatientId());

        String path = streamFile.buildPathAsString(event.getRelativeDirectory());
        String realFileName = event.getUuid();
        String fictitiousFileName = event.buildDownloadName();
        String checksum = null;
        try {
            ByteArrayOutputStream output =  pdfService.writer(event.getTemplateName(), contextMap);
            streamFile.saveFileInDirectory(path, false, output);
            checksum = getHash(path);
        } catch (IOException | PDFDocumentException e) {
            LOG.error("Save document file -> {}", event, e);
        }
        return Optional.of(new DocumentFile(
                event.getDocument().getId(),
                event.getSourceId(),
                event.getSourceType(),
                event.getDocumentTypeId(), path, fictitiousFileName, realFileName, checksum));
    }

    private static String getHash(String path) {
        LOG.debug("Input parameters -> path {}", path);
        String result;
        String algorithm = "SHA-256";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] sha256Hash = md.digest(Files.readAllBytes(Paths.get(path)));
            result = Base64.getEncoder().encodeToString(sha256Hash);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Algorithm doesn't exist -> {} ",algorithm);
            result = null;
        }
        catch (IOException e) {
            LOG.error("Error with path file {} ", path, e);
            result = null;
        }
        LOG.debug(OUTPUT, result);
        return result;
    }

}
