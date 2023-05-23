package net.pladema.edMonton.getPdfEdMonton.controller;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;




public class GetPdfEdMontoController implements GetPdfEdMontonAPI{

	private static final Logger LOG = LoggerFactory.getLogger(GetPdfEdMontoController.class);


	@Override
	public ResponseEntity<InputStreamResource> getEdMontonTestPdf(
			Integer edMontonId) throws IOException, PDFDocumentException {
		LOG.debug("input parameter -> edMontonId{}", edMontonId);

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));




		return null;
	}
}
