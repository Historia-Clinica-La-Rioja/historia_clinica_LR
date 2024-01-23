package net.pladema.questionnaires.general.getpdf.controller;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

import com.lowagie.text.DocumentException;

import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;
import net.pladema.questionnaires.general.getpdf.domain.service.GetQuestionnairePdfService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Patient questionnaires and assessments", description = "Patient questionnaires and assessments")
public class GetQuestionnairePdfController {
	private final Logger logger = LoggerFactory.getLogger(GetQuestionnairePdfController.class);

	@Autowired
	private final GetQuestionnairePdfService getQuestionnairePdfService;

	@Autowired
	private final QuestionnaireResponseRepository questionnaireResponseRepository;

	@Autowired
	private final PdfService pdfService;

    public GetQuestionnairePdfController(GetQuestionnairePdfService getQuestionnairePdfService, QuestionnaireResponseRepository questionnaireResponseRepository, PdfService pdfService) {
        this.getQuestionnairePdfService = getQuestionnairePdfService;
        this.questionnaireResponseRepository = questionnaireResponseRepository;
        this.pdfService = pdfService;
    }

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("institution/{institutionId}/questionnaire/{questionnaireResponseId}/get-pdf")
	public ResponseEntity<InputStreamResource> getQuestionnairePdf(
			@PathVariable Integer questionnaireResponseId,
			@PathVariable Integer institutionId) throws PDFDocumentException, DocumentException, IOException {
		logger.debug("input parameter -> questionnaireResponseId: {}", questionnaireResponseId);

		QuestionnaireResponse response = questionnaireResponseRepository.findById(questionnaireResponseId)
				.orElseThrow(() -> new NotFoundException("Questionnaire response not found with id %s"));

		String outputFileName = getQuestionnairePdfService.createQuestionnaireFileName(response);

		Map<String, Object> context = getQuestionnairePdfService.createQuestionnaireContext(questionnaireResponseId, institutionId);

		Integer questionnaireId = response.getQuestionnaireId();

		try {
			return generatePdfResponse(context, outputFileName, questionnaireId);
		} catch (IOException e) {
			logger.error("Error generating PDF response", e);
			throw new PDFDocumentException(e);
		}
	}

	private ResponseEntity<InputStreamResource> generatePdfResponse(Map<String, Object> context, String outputFileName, Integer questionnaireId) throws IOException {
		logger.debug("input parameters -> context: {}, outputFileName {}, questionnaireId: {}", context, outputFileName, questionnaireId);
		String templateName = "";
		if (questionnaireId == 1) {
			templateName = "edmonton_reports";
		}
		if (questionnaireId == 2) {
			templateName = "familybg_reports";
		}
		if (questionnaireId == 3) {
			templateName = "frail_reports";
		}
		if (questionnaireId == 4) {
			templateName = "physicalperformance_reports";
		}

		FileContentBo fileContentBo = pdfService.generate(templateName, context);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			fileContentBo.getStream().transferTo(outputStream);
		} catch (IOException e) {
			logger.error("Error transferring stream to output stream", e);
		}

		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		InputStreamResource resource = new InputStreamResource(inputStream);
		ResponseEntity<InputStreamResource> response;
		response = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + outputFileName).contentType(MediaType.APPLICATION_PDF).contentLength(outputStream.size()).body(resource);

		return response;
	}
}
