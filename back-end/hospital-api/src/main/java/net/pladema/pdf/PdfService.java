package net.pladema.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import net.pladema.internation.controller.documents.anamnesis.AnamnesisController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PdfService {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisController.class);

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
        File htmlFile = new File(name + ".html");
        File pdfFile = new File(name + ".pdf");
        ResponseEntity<InputStreamResource> response;
        try {
            htmlFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(htmlFile);
            fos.write(templateEngine.process(templateName, ctx).getBytes());
            fos.close();
            pdfFile.createNewFile();
            FileOutputStream fosPDF = new FileOutputStream(name + ".pdf");
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, fosPDF);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(htmlFile));
            document.close();
            htmlFile.delete();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));
            response = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + pdfFile.getName())
                    .contentType(MediaType.APPLICATION_PDF).contentLength(pdfFile.length()).body(resource);
            pdfFile.delete();
        } catch (Exception e){
            htmlFile.delete();
            pdfFile.delete();
            throw e;
        }
        return response;
    }
}
