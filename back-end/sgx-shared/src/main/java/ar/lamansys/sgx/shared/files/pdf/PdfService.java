package ar.lamansys.sgx.shared.files.pdf;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

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

    public PdfService(
            FlavorService flavorService,
            SpringTemplateEngine templateEngine
    ) {
        super();

        this.calculateTemplateNameWithFlavor = templateName -> String.format("flavors/%s/%s", flavorService.getFlavor().toString(), templateName);
        this.templateEngine = templateEngine;

    }

	public FileContentBo generate(String templateName, Map<String,Object> contextMap) throws PDFDocumentException {
		Context context = buildContext(contextMap);
		String templateNameWithFlavor = calculateTemplateNameWithFlavor.apply(templateName);
		String generatedHTML = templateEngine.process(templateNameWithFlavor, context);
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
