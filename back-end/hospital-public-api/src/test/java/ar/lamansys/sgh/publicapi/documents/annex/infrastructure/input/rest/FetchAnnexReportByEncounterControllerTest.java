package ar.lamansys.sgh.publicapi.documents.annex.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.documents.annex.application.FetchAnnexReportByEncounter;

import ar.lamansys.sgh.publicapi.documents.annex.application.exception.FetchAnnexReportByEncounterException;
import ar.lamansys.sgh.shared.domain.annex.SharedAppointmentAnnexPdfReportResponse;

import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchAnnexReportByEncounterControllerTest {

	FetchAnnexReportByEncounterController controller;

	@Mock
	FetchAnnexReportByEncounter fetchAnnexReportByEncounter;

	@BeforeEach
	void setup() {
		controller = new FetchAnnexReportByEncounterController(fetchAnnexReportByEncounter);
	}

	@Test
	public void controller_returns_file_contents() throws FetchAnnexReportByEncounterException, IOException {
		String refsetCode = "xxx";
		Integer encounterId = 1;
		String scope = "XX";
		String pdfContent = "pdf annex II";

		byte[] pdf = pdfContent.getBytes();

		when(fetchAnnexReportByEncounter.run(refsetCode, encounterId, scope)).thenReturn(new SharedAppointmentAnnexPdfReportResponse() {
			@Override
			public FileContentBo getPdf() {
				try {
					return FileContentBo.fromBytes(pdf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public String getFilename() {
				return "Success";
			}
		});

		var ret = controller.getAnnexIIReportByEncounterId(refsetCode, encounterId, scope);

		assertEquals(ret.getStatusCode(), HttpStatus.OK);
		byte[] readBytes = new byte[pdf.length];
		ret.getBody().getInputStream().read(readBytes);
		assertEquals(new String(readBytes), new String(pdf));
	}
}
