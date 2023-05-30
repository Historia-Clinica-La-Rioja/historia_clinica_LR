package net.pladema.edMonton.getPdfEdMonton.controller;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

public interface GetPdfEdMontonAPI {

	@GetMapping("edMonton/{edMontonId}/pdf-download")
	ResponseEntity<InputStreamResource> getEdMontonTestPdf(
			@PathVariable(name = "edMontonId") Long edMontonId) throws IOException, PDFDocumentException;
}
