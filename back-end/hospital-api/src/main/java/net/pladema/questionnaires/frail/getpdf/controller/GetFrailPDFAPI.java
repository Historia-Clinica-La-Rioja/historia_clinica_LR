package net.pladema.questionnaires.frail.getpdf.controller;

import java.io.IOException;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;

@Tag(name = "Patient consultation - Frail", description = "Patient consultation - Frail")
public interface GetFrailPDFAPI {

	@GetMapping("frail/{questionnaireId}/pdf-download")
	ResponseEntity<InputStreamResource> getQuestionnaireTestPDF(@PathVariable(name = "questionnaireId") Long questionnaireId) throws IOException, PDFDocumentException;
}
