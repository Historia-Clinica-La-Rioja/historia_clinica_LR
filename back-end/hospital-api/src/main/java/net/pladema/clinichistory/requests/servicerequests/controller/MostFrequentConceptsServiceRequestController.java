package net.pladema.clinichistory.requests.servicerequests.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.application.GetMostFrequentTemplates;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.SnomedTemplateMapper;
import net.pladema.clinichistory.requests.servicerequests.service.GetMostFrequentConceptsService;

import net.pladema.snowstorm.controller.dto.SnomedTemplateDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/service-request/most-frequent")
@Tag(name = "Most Frequent Concepts", description = "Most Frequent Concepts Service Request")
@Validated
@Slf4j
@RequiredArgsConstructor
public class MostFrequentConceptsServiceRequestController {

	private final GetMostFrequentConceptsService getMostFrequentConceptsService;

	private final GetMostFrequentTemplates getMostFrequentTemplates;

	private final SnomedTemplateMapper snomedTemplateMapper;

	@GetMapping("/concepts")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<SharedSnomedDto>> getMostFrequentConceptStudies(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<SharedSnomedDto> result = getMostFrequentConceptsService.getMostFrequentStudies(institutionId);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/get-templates")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD')")
	public List<SnomedTemplateDto> getMostFrequentTemplates(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input data -> institutionId: {}", institutionId);
		List<SnomedTemplateDto> result = getMostFrequentTemplates.run(institutionId)
				.stream()
				.map(snomedTemplateMapper::mapToSnomedTemplateDto)
				.sorted(Comparator.comparing(SnomedTemplateDto::getDescription))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}
}
