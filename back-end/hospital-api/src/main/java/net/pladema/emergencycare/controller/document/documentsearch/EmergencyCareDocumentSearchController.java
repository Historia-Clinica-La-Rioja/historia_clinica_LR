package net.pladema.emergencycare.controller.document.documentsearch;

import ar.lamansys.sgh.clinichistory.application.searchDocument.DocumentSearchService;
import ar.lamansys.sgh.clinichistory.application.searchDocument.EmergencyCareEpisodeTriageSearchBo;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.mapper.DocumentSearchMapper;
import net.pladema.emergencycare.controller.document.documentsearch.dto.EmergencyCareHistoricDocumentDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/emergency-care/{emergencyCareEpisodeId}/documentSearch")
@Tag(name = "Emergency Care Document search", description = "Emergency Care Document search")
@Validated
@AllArgsConstructor
public class EmergencyCareDocumentSearchController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareDocumentSearchController.class);

	public static final String OUTPUT = "Output -> {}";

	private final DocumentSearchService documentSearchService;

	private final DocumentSearchMapper documentSearchMapper;

	@GetMapping
	public ResponseEntity<EmergencyCareHistoricDocumentDto> getEmergencyCareHistoricDocumentList(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "emergencyCareEpisodeId") Integer emergencyCareEpisodeId){
		LOG.debug("Input parameters -> institutionId {}, emergencyCareEpisodeId {}", institutionId, emergencyCareEpisodeId);
		List<EmergencyCareEpisodeTriageSearchBo> documentTriageHistoric = documentSearchService.getEmergencyCareTriageHistoricalDocumentList(emergencyCareEpisodeId);
		EmergencyCareHistoricDocumentDto result = new EmergencyCareHistoricDocumentDto(documentSearchMapper.toEmergencyCareEpisodeTriageSearchListDto(documentTriageHistoric));
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

}
