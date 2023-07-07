package net.pladema.edMonton.getPdfEdMonton.controller;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;

import ar.lamansys.sgx.shared.files.pdf.PdfService;

import net.pladema.edMonton.getPdfEdMonton.dto.ImpresionEdMontonDto;

import net.pladema.edMonton.getPdfEdMonton.service.ImpresionEdMontonService;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;


@RestController
@Validated
@RequestMapping("/institution/patient/outpatient/consultation/")
public class GetPdfEdMontoController implements GetPdfEdMontonAPI{

	private static final Logger LOG = LoggerFactory.getLogger(GetPdfEdMontoController.class);

	private final ImpresionEdMontonService impresionEdMontonService;

	public static final String OUTPUT = "Output -> {}";



	private final PdfService pdfService;

	public GetPdfEdMontoController(ImpresionEdMontonService impresionEdMontonService, PdfService pdfService) {
		this.impresionEdMontonService = impresionEdMontonService;
		this.pdfService = pdfService;
	}


	@Override
	public ResponseEntity<InputStreamResource> getEdMontonTestPdf(
			Long edMontonId) throws IOException, PDFDocumentException {
		LOG.debug("input parameter -> edMontonId{}", edMontonId);

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
		List<ImpresionEdMontonDto> impresionEdMontonDto = impresionEdMontonService.getImpresionEdMonton(edMontonId);
		Object resultFinal = impresionEdMontonService.getScore(edMontonId);

		Map<String, Object> context = impresionEdMontonService.createEdMontonContext(impresionEdMontonDto, resultFinal);

		String outputFileName = impresionEdMontonService.createEdMontonFileName(edMontonId, now);
		ResponseEntity<InputStreamResource> response = generatedPdfResponse(context, outputFileName, "edMonton_reports");

		return response;
	}

	private ResponseEntity<InputStreamResource> generatedPdfResponse(Map<String, Object> context, String outPutFileName, String templateName){
		LOG.debug("input parameters -> conext {}, outputFilaName{}", templateName);
		ByteArrayOutputStream outputStream = pdfService.writer(templateName, context);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
		InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
		ResponseEntity<InputStreamResource> response;
		response = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + outPutFileName)
				.contentType(MediaType.APPLICATION_PDF).contentLength(outputStream.size()).body(resource);

		return response;
	}
}
