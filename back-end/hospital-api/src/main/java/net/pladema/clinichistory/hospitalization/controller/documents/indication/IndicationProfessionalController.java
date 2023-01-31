package net.pladema.clinichistory.hospitalization.controller.documents.indication;

import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;

import net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.ProfessionalParenteralPlanService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.ProfessionalPharmacoService;

@RestController
@RequestMapping("/institutions/{institutionId}/indication")
@Tag(name = "Indication by Professional", description = "Indication by Professional")
@Validated
@Slf4j
@RequiredArgsConstructor
public class IndicationProfessionalController {

	private final ProfessionalPharmacoService professionalPharmacoService;
	private final ProfessionalParenteralPlanService professionalParenteralPlanService;

	@GetMapping("/pharmacos/most-frequent")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<PharmacoSummaryDto>> getMostFrequentPharmacosIndicatedByProfessional(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<PharmacoSummaryDto> result = professionalPharmacoService.getMostFrequentPharmacos(institutionId);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/parenteralplans/most-frequent")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<ParenteralPlanDto>> getMostFrequentParenteralPlansIndicatedByProfessional(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<ParenteralPlanDto> result = professionalParenteralPlanService.getMostFrequentParenteralPlans(institutionId);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}
