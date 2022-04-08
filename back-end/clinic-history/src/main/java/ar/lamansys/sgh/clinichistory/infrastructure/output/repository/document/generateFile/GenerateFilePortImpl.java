package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.document.event.GenerateFilePort;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentFile;
import ar.lamansys.sgx.shared.files.StreamFile;
import ar.lamansys.sgx.shared.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.pdf.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class GenerateFilePortImpl implements GenerateFilePort {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateFilePortImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final StreamFile streamFile;

    private final PdfService pdfService;

    private final AuditableContextBuilder auditableContextBuilder;

    public GenerateFilePortImpl(
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
        Map<String,Object> contextMap = auditableContextBuilder.buildContext(event.getDocumentBo(), event.getPatientId());

		formatStringDates(contextMap);

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
                event.getDocumentBo().getId(),
                event.getEncounterId(),
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

	private void formatStringDates(Map<String, Object> context){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		ArrayList<ImmunizationInfoDto> vaccinesData = (ArrayList<ImmunizationInfoDto>) context.get("nonBillableImmunizations");
		vaccinesData.stream().filter(immunizationInfoDto -> immunizationInfoDto.getAdministrationDate() != null)
				.forEach(immunizationInfoDto -> immunizationInfoDto.setAdministrationDate(LocalDate.parse(immunizationInfoDto.getAdministrationDate()).format(dateTimeFormatter)));
	}

}
