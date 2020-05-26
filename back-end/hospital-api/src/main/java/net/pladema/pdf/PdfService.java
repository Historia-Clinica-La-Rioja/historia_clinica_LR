package net.pladema.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import net.pladema.internation.controller.internment.dto.ResponsibleDoctorDto;
import net.pladema.internation.controller.internment.mapper.ResponsibleDoctorMapper;
import net.pladema.internation.controller.ips.dto.VitalSignsReportDto;
import net.pladema.internation.controller.ips.mapper.VitalSignMapper;
import net.pladema.internation.events.OnGenerateDocumentEvent;
import net.pladema.internation.repository.documents.DocumentFileRepository;
import net.pladema.internation.repository.documents.entity.DocumentFile;
import net.pladema.internation.service.documents.InternmentDocument;
import net.pladema.internation.service.documents.ReportDocumentService;
import net.pladema.patient.controller.service.PatientExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.*;
import java.util.Locale;

@Component
public class PdfService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfService.class);

    public static final String OUTPUT = "Output -> {}";

    private final SpringTemplateEngine templateEngine;

    private final StreamFile streamFile;

    private final PatientExternalService patientExternalService;

    private final ReportDocumentService reportDocumentService;

    private final ResponsibleDoctorMapper responsibleDoctorMapper;

    private final VitalSignMapper vitalSignMapper;

    private final DocumentFileRepository documentFileRepository;

    public PdfService(SpringTemplateEngine templateEngine,
                      StreamFile streamFile,
                      PatientExternalService patientExternalService,
                      ReportDocumentService reportDocumentService,
                      ResponsibleDoctorMapper responsibleDoctorMapper,
                      VitalSignMapper vitalSignMapper,
                      DocumentFileRepository documentFileRepository) {
        super();
        this.templateEngine = templateEngine;
        this.streamFile = streamFile;
        this.patientExternalService = patientExternalService;
        this.reportDocumentService = reportDocumentService;
        this.responsibleDoctorMapper = responsibleDoctorMapper;
        this.vitalSignMapper = vitalSignMapper;
        this.documentFileRepository = documentFileRepository;
    }

    public ResponseEntity<InputStreamResource> getResponseEntityPdf(String name, String templateName,Context ctx)
            throws IOException, DocumentException {
        LOG.debug("Input parameters -> name {}, templateName {}, ctx {}",
                name, templateName, ctx);

        ByteArrayOutputStream writer = writer(templateName, ctx);
        return reader(writer, streamFile.buildDownloadName(name));
    }

    private ByteArrayOutputStream writer(String templateName, Context ctx) throws IOException, DocumentException {
        try {
            Document document = new Document();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.open();
            InputStream is = new ByteArrayInputStream(templateEngine.process(templateName, ctx).getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            writer.close();
            return os;
        } catch (IOException e){
            throw new IOException(e);
        }
        catch(DocumentException e) {
            throw new DocumentException(e);
        }
    }

    private ResponseEntity<InputStreamResource> reader(ByteArrayOutputStream os, String name) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        ResponseEntity<InputStreamResource> response;
        response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name)
                .contentType(MediaType.APPLICATION_PDF).contentLength(os.size()).body(resource);
        return response;
    }

    @Async
    @EventListener
    //TODO Add event listener condition like "#event.document.confirmed"
    public void loadDocument(OnGenerateDocumentEvent event) throws IOException, DocumentException {
        LOG.debug("Input parameters -> onGenerateDocumentEvent {}", event);
        if(event.getDocument().isConfirmed()) {
            Context context = buildContext(event.getDocument(), event.getPatiendId());
            ByteArrayOutputStream writer = writer(event.getTemplateName(), context);
            String path = streamFile.buildPath(event.getInstitutionId(), event.getInternmentEpisodeId(), event.getDocumentType());
            String realFileName = streamFile.buildUUIDName();
            String ficticiusFileName = streamFile.buildDownloadName(event.getDocument().getId(), event.getDocumentType());

            try {
                boolean loaded = streamFile.loadFileInDirectory(path, writer);
                if (loaded)
                    documentFileRepository.save(new DocumentFile(
                            event.getDocument().getId(),
                            event.getInternmentEpisodeId(),
                            event.getDocumentType(), path, ficticiusFileName, realFileName));
            } catch (IOException ex) {
                throw new IOException(ex);
            }
        }
    }

    public <T extends InternmentDocument> Context buildContext(T document, Integer patientId){
        LOG.debug("Input parameters -> document {}", document);

        ResponsibleDoctorDto author = responsibleDoctorMapper.toResponsibleDoctorDto(
                reportDocumentService.getAuthor(document.getId()));

        VitalSignsReportDto vitalSignsReportDto = vitalSignMapper.toVitalSignsReportDto(document.getVitalSigns());
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("patient", patientExternalService.getBasicDataFromPatient(patientId));
        ctx.setVariable("mainDiagnosis", document.getMainDiagnosis());
        ctx.setVariable("diagnosis", document.getDiagnosis());
        ctx.setVariable("personalHistories", document.getPersonalHistories());
        ctx.setVariable("familyHistories", document.getFamilyHistories());
        ctx.setVariable("allergies", document.getAllergies());
        ctx.setVariable("inmunizations", document.getInmunizations());
        ctx.setVariable("medications", document.getMedications());
        ctx.setVariable("anthropometricData", document.getAnthropometricData());
        ctx.setVariable("vitalSigns", vitalSignsReportDto);
        ctx.setVariable("notes", document.getNotes());
        ctx.setVariable("author", author);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }
}
