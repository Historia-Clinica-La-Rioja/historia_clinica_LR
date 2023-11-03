package ar.lamansys.sgx.shared.files.pdf;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import com.lowagie.text.Document;

import com.lowagie.text.DocumentException;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

import com.lowagie.text.pdf.PdfWriter;

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

	private final Logger LOG = LoggerFactory.getLogger(PdfService.class);

	private final Function<String, String> calculateTemplateNameWithFlavor;

    private final SpringTemplateEngine templateEngine;

	private final CustomizableTemplateEngine customizableTemplateEngine;

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
	
	public byte[] mergePdfFiles(List<InputStream> inputList) throws IOException, DocumentException {
		LOG.debug("Input parameter -> inputList {}", inputList);
		Document document = new Document();
		List<PdfReader> readers = new ArrayList<>();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Iterator<InputStream> pdfIterator = inputList.iterator();
		while (pdfIterator.hasNext()) {
			InputStream pdf = pdfIterator.next();
			PdfReader pdfReader = new PdfReader(pdf);
			readers.add(pdfReader);
		}
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		document.open();
		PdfContentByte pageContentByte = writer.getDirectContent();
		PdfImportedPage pdfImportedPage;
		int currentPdfReaderPage = 1;
		Iterator<PdfReader> iteratorPDFReader = readers.iterator();
		while (iteratorPDFReader.hasNext()) {
			PdfReader pdfReader = iteratorPDFReader.next();
			while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
				document.newPage();
				pdfImportedPage = writer.getImportedPage(
						pdfReader, currentPdfReaderPage);
				pageContentByte.addTemplate(pdfImportedPage, 0, 0);
				currentPdfReaderPage++;
			}
			currentPdfReaderPage = 1;
		}
		outputStream.flush();
		document.close();
		outputStream.close();
		byte[] result = outputStream.toByteArray();
		LOG.debug("Output -> {}", result);
		return result;
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
