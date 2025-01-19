package ar.lamansys.sgh.publicapi.documents.annex.infrastructure.input.rest;


import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.documents.annex.application.FetchAnnexReportByEncounter;
import ar.lamansys.sgh.publicapi.documents.annex.application.exception.FetchAnnexReportByEncounterException;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Tag(name = "PublicApi Documents", description = "Annex report")
@RequestMapping("/public-api/institution/refset/{refsetCode}/documents/encounter/{encounterId}/scope/{scope}/annex")
@RestController
public class FetchAnnexReportByEncounterController {

	private final FetchAnnexReportByEncounter fetchAnnexReportByEncounter;

	@GetMapping
	public ResponseEntity<Resource> getAnnexIIReportByEncounterId(
		@PathVariable("refsetCode") String refsetCode,
		@PathVariable("encounterId") Integer encounterId,
		@PathVariable("scope") String scope
	) throws FetchAnnexReportByEncounterException {
		var report = fetchAnnexReportByEncounter.run(refsetCode, encounterId, scope);
		return StoredFileResponse.sendFile(
				report.getPdf(),
				report.getFilename(),
				MediaType.APPLICATION_PDF
		);
	}

}
