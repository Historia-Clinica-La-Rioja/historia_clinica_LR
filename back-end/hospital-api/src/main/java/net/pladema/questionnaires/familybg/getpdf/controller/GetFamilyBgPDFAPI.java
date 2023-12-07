package net.pladema.questionnaires.familybg.getpdf.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;

public interface GetFamilyBgPDFAPI {

	@GetMapping("familybg/{questionnaireId}/pdf-download")
	ResponseEntity<InputStreamResource> getQuestionnaireTestPDF(@PathVariable(name = "questionnaireId") Long questionnaireId) throws IOException, PDFDocumentException;
}
