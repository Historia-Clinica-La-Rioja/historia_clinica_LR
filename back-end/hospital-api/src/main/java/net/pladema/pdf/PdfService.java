package net.pladema.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PdfService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfService.class);

    public static final String OUTPUT = "Output -> {}";

    private final SpringTemplateEngine templateEngine;

    public PdfService(SpringTemplateEngine templateEngine) {
        super();
        this.templateEngine = templateEngine;
    }

    public ResponseEntity<InputStreamResource> getResponseEntityPdf(String name, String templateName, LocalDateTime time,
                                                                    Context ctx) throws IOException, DocumentException {
        LOG.debug("Input parameters -> name {}, templateName {}, time {}, ctx {}",
                name, templateName, time,  ctx);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        name = name + "_" + formattedDateTime;
        File pdfFile = new File(name + ".pdf");
        ResponseEntity<InputStreamResource> response;
        try {
            OutputStream file = new FileOutputStream(pdfFile);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();
            InputStream is = new ByteArrayInputStream(templateEngine.process(templateName, ctx).getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            file.close();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));
            resource.getDescription();
            response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + pdfFile.getName())
                    .contentType(MediaType.APPLICATION_PDF).contentLength(pdfFile.length()).body(resource);
            pdfFile.delete();
        } catch (Exception e){
            pdfFile.delete();
            throw e;
        }
        return response;
    }
}
