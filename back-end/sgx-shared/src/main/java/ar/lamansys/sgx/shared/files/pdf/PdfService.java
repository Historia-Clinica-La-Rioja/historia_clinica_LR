package ar.lamansys.sgx.shared.files.pdf;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import com.lowagie.text.DocumentException;
import ar.lamansys.sgx.shared.templating.CustomizableTemplateEngine;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.flavor.FlavorService;

@Component
public class PdfService {

    private final Function<String, String> calculateTemplateNameWithFlavor;

    private final SpringTemplateEngine templateEngine;

	private final CustomizableTemplateEngine customizableTemplateEngine;

	private static final Logger LOG = LoggerFactory.getLogger(PdfService.class);

	public static final String OUTPUT = "Output -> {}";


	public PdfService(
            SpringTemplateEngine templateEngine,
			FlavorService flavorService,
			CustomizableTemplateEngine customizableTemplateEngine
    ) {
        super();

        this.calculateTemplateNameWithFlavor = templateName -> String.format("flavors/%s/%s", flavorService.getFlavor().toString(), templateName);
        this.templateEngine = templateEngine;
		this.customizableTemplateEngine = customizableTemplateEngine;

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

	private InputStream templateEngineProcess(String templateName, Context context) {
		String templateNameWithFlavor = calculateTemplateNameWithFlavor.apply(templateName);
		return new ByteArrayInputStream(templateEngine.process(templateNameWithFlavor, context).getBytes());
	}

	private static ByteArrayOutputStream writeAndGetOutput(InputStream is, ByteArrayOutputStream os)
			throws IOException, DocumentException {
		String processedHtml = IOUtils.toString(is, StandardCharsets.UTF_8);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(processedHtml, "");
		renderer.layout();
		renderer.createPDF(os, false);
		renderer.finishPDF();
		return os;
	}


	public FileContentBo generate(String templateName, Map<String,Object> contextMap) throws PDFDocumentException {
		Context context = buildContext(contextMap);
		String templateNameWithFlavor = calculateTemplateNameWithFlavor.apply(templateName);
		String generatedHTML = templateEngine.process(templateNameWithFlavor, context);
		return buildResponse(generatedHTML);
	}

	public FileContentBo customizableGenerate(String templateName, Map<String,Object> contextMap) throws PDFDocumentException {
		Context context = buildContext(contextMap);
		String generatedHTML = customizableTemplateEngine.process(templateName, context);
		return buildResponse(generatedHTML);
	}

    private static Context buildContext(Map<String,Object> contextMap) {
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariables(contextMap);
        return ctx;
    }


	private FileContentBo buildResponse(String processedHtml) {
		return StreamsUtils.writeToContent((out) -> {
			try {
				ITextRenderer renderer = new ITextRenderer();
				renderer.setDocumentFromString(processedHtml, "");
				renderer.layout();
				renderer.createPDF(out, false);
				renderer.finishPDF();
			} catch (Exception e) {
				throw streamException(e);
			}
		});
	}


}
