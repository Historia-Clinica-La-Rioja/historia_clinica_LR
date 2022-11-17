package ar.lamansys.sgx.shared.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import ar.lamansys.sgx.shared.flavor.FlavorService;

@Component
public class PdfService {

    private static final Logger LOG = LoggerFactory.getLogger(PdfService.class);

    public static final String OUTPUT = "Output -> {}";

    private final Function<String, String> calculateTemplateNameWithFlavor;

    private final SpringTemplateEngine templateEngine;

    public PdfService(
            FlavorService flavorService,
            SpringTemplateEngine templateEngine
    ) {
        super();

        this.calculateTemplateNameWithFlavor = templateName -> String.format("flavors/%s/%s", flavorService.getFlavor().toString(), templateName);
        this.templateEngine = templateEngine;

    }

    public ByteArrayOutputStream writer(String templateName, Map<String,Object> contextMap) throws PDFDocumentException {
        Context context = buildContext(contextMap);

        try (InputStream is = templateEngineProcess(templateName, context); ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            return writeAndGetOutput(is, os);
        } catch (IOException | DocumentException e){
			LOG.error("Error para generar el siguiente template {}, en este contexto {}", templateName, contextMap);
            throw new PDFDocumentException(e);
        }
    }

    public ByteArrayInputStream reader(String path) throws PDFDocumentException {
        LOG.debug("Input parameters -> path {}", path);
        try {
            Path pdfPath = Paths.get(path);
            byte[] pdf = Files.readAllBytes(pdfPath);
            LOG.debug("Output -> path {}", path);
            return new ByteArrayInputStream(pdf);
        }  catch (IOException e){
            LOG.error("Leyendo PDF file {}", path);
            throw new PDFDocumentException(e);
        }
    }

    private InputStream templateEngineProcess(String templateName, Context context) {
        String templateNameWithFlavor = calculateTemplateNameWithFlavor.apply(templateName);
        return new ByteArrayInputStream(templateEngine.process(templateNameWithFlavor, context).getBytes());
    }

    private static Context buildContext(Map<String,Object> contextMap) {
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariables(contextMap);
        return ctx;
    }

    private static ByteArrayOutputStream writeAndGetOutput(InputStream is, ByteArrayOutputStream os)
            throws IOException, DocumentException {
        String processedHtml = IOUtils.toString(is, StandardCharsets.UTF_8.name());
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml, "");
        renderer.layout();
        renderer.createPDF(os, false);
        renderer.finishPDF();
        return os;
    }



}
