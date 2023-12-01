package net.pladema.questionnaires.edmonton.getpdf.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;

@Tag(name = "Patient consultation - Edmonton", description = "Patient consultation - Edmonton")
public interface GetEdmontonPDFAPI {

	@GetMapping("edmonton/{questionnaireId}/pdf-download")
	ResponseEntity<InputStreamResource> getQuestionnaireTestPDF(@PathVariable(name = "questionnaireId") Long questionnaireId) throws IOException, PDFDocumentException;
}
