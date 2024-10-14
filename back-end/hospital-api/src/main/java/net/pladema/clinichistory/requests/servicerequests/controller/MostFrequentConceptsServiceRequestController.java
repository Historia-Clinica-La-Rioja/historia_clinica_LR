package net.pladema.clinichistory.requests.servicerequests.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.service.GetMostFrequentConceptsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/service-request/most-frequent")
@Tag(name = "Most Frequent Concepts", description = "Most Frequent Concepts Service Request")
@Validated
@Slf4j
@RequiredArgsConstructor
public class MostFrequentConceptsServiceRequestController {

	private final GetMostFrequentConceptsService getMostFrequentConceptsService;

	@GetMapping("/concepts")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<SharedSnomedDto>> getMostFrequentConceptStudies(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<SharedSnomedDto> result = getMostFrequentConceptsService.getMostFrequentStudies(institutionId);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}
