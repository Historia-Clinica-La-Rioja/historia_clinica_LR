package net.pladema.emergencycare.controller.document.documentsearch;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;

import net.pladema.emergencycare.controller.document.documentsearch.dto.EmergencyCareHistoricDocumentDto;

import net.pladema.emergencycare.triage.controller.dto.TriageDocumentDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.mapper.TriageListMapper;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/emergency-care/{emergencyCareEpisodeId}/documentSearch")
@Tag(name = "Emergency Care Document search", description = "Emergency Care Document search")
@Validated
@AllArgsConstructor
public class EmergencyCareDocumentSearchController {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareDocumentSearchController.class);

	public static final String OUTPUT = "Output -> {}";

	private final TriageService triageService;

	private final TriageListMapper triageListMapper;

	private final DocumentService documentService;

	@GetMapping
	public ResponseEntity<EmergencyCareHistoricDocumentDto> getEmergencyCareHistoricDocumentList(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "emergencyCareEpisodeId") Integer emergencyCareEpisodeId){
		LOG.debug("Input parameters -> institutionId {}, emergencyCareEpisodeId {}", institutionId, emergencyCareEpisodeId);
		List<TriageBo> triageHistoric = triageService.getAll(institutionId, emergencyCareEpisodeId);
		List<TriageListDto> triages = triageHistoric.stream().map(triageListMapper::toTriageListDto).collect(Collectors.toList());
		List<TriageDocumentDto> triageAndDocument = new ArrayList<>();
		triages.forEach(triage -> {
			triageAndDocument.add(new TriageDocumentDto(triage, documentService.getDocumentIdByTriage(triage.getId())));
		});
		EmergencyCareHistoricDocumentDto result = new EmergencyCareHistoricDocumentDto(triageAndDocument);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

}
