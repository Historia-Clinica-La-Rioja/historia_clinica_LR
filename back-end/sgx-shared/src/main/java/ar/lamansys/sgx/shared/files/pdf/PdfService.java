package ar.lamansys.sgx.shared.files.pdf;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.flavor.FlavorService;
import ar.lamansys.sgx.shared.stats.TimeProfilingUtil;
import ar.lamansys.sgx.shared.templating.CustomizableTemplateEngine;

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

	public FileContentBo generate(String templateName, Map<String,Object> contextMap) throws PDFDocumentException {
		Context context = buildContext(contextMap);
		String templateNameWithFlavor = calculateTemplateNameWithFlavor.apply(templateName);
		String generatedHTML = templateEngine.process(templateNameWithFlavor, context);
		return buildResponse(generatedHTML, templateName);
	}

	public FileContentBo customizableGenerate(String templateName, Map<String,Object> contextMap) throws PDFDocumentException {
		Context context = buildContext(contextMap);
		String generatedHTML = customizableTemplateEngine.process(templateName, context);
		return buildResponse(generatedHTML, templateName);
	}

    private static Context buildContext(Map<String,Object> contextMap) {
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariables(contextMap);
        return ctx;
    }


	private FileContentBo buildResponse(String processedHtml, String templateName) {
		return StreamsUtils.writeToContent((out) -> {
			try {
				var htmlStat = TimeProfilingUtil.start("Process HTML");
				ITextRenderer renderer = new ITextRenderer();
				renderer.setDocumentFromString(processedHtml, "");
				renderer.layout();
				htmlStat.done(templateName);

				var pdfStat = TimeProfilingUtil.start("Generate PDF");
				renderer.createPDF(out, false);
				renderer.finishPDF();
				pdfStat.done(templateName);

			} catch (Exception e) {
				throw streamException(e);
			}
		});
	}


}
