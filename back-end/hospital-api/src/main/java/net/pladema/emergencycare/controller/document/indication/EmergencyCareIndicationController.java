package net.pladema.emergencycare.controller.document.indication;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.controller.mapper.IndicationMapper;
import net.pladema.clinichistory.indication.service.diet.DietService;
import net.pladema.clinichistory.indication.service.otherindication.OtherIndicationService;
import net.pladema.clinichistory.indication.service.parenteralplan.ParenteralPlanService;
import net.pladema.clinichistory.indication.service.pharmaco.PharmacoService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/emergency-care/episode/{episodeId}")
@Tag(name = "Emergency Care Indication", description = "Emergency Care Indication")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EmergencyCareIndicationController {

	private final DietService dietService;

	private final OtherIndicationService otherIndicationService;

	private final PharmacoService pharmacoService;

	private final ParenteralPlanService parenteralPlanService;

	private final IndicationMapper indicationMapper;

	@GetMapping("/diets")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<DietDto>> getEmergencyCareEpisodeDiets(@PathVariable(name = "institutionId") Integer institutionId,
																	  @PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<DietDto> result = dietService.getEpisodeDiets(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/diet")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<Integer> addDiet(@PathVariable(name = "institutionId") Integer institutionId,
										   @PathVariable(name = "episodeId") Integer episodeId,
										   @RequestBody DietDto dietDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, dietDto {}", institutionId, episodeId, dietDto);
		Integer result = dietService.addDiet(indicationMapper.mapToDietBo(dietDto, institutionId, episodeId, SourceType.EMERGENCY_CARE));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/other-indication")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<Integer> addOtherIndication(@PathVariable(name = "institutionId") Integer institutionId,
													  @PathVariable(name = "episodeId") Integer episodeId,
													  @RequestBody OtherIndicationDto otherIndicationDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, otherIndicationDto {}", institutionId, episodeId, otherIndicationDto);
		Integer result = otherIndicationService.add(indicationMapper.mapToOtherIndicationBo(otherIndicationDto, institutionId, episodeId, SourceType.EMERGENCY_CARE));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/other-indications")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<OtherIndicationDto>> getEmergencyCareEpisodeOtherIndications(@PathVariable(name = "institutionId") Integer institutionId,
																							@PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<OtherIndicationDto> result = otherIndicationService.getEpisodeOtherIndications(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/pharmaco")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<Integer> addPharmaco(@PathVariable(name = "institutionId") Integer institutionId,
											   @PathVariable(name = "episodeId") Integer episodeId,
											   @RequestBody PharmacoDto pharmacoDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, pharmacoDto {}", institutionId, episodeId, pharmacoDto);
		Integer result = pharmacoService.add(indicationMapper.mapToPharmacoBo(pharmacoDto, institutionId, episodeId, SourceType.EMERGENCY_CARE));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pharmacos")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<PharmacoSummaryDto>> getEmergencyCareEpisodePharmacos(@PathVariable(name = "institutionId") Integer institutionId,
																					 @PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<PharmacoSummaryDto> result = pharmacoService.getEpisodePharmacos(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/parenteral-plan")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<Integer> addParenteralPlan(@PathVariable(name = "institutionId") Integer institutionId,
													 @PathVariable(name = "episodeId") Integer episodeId,
													 @RequestBody ParenteralPlanDto parenteralPlan) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, parenteralPlanDto {}", institutionId, episodeId, parenteralPlan);
		Integer result = parenteralPlanService.add(indicationMapper.mapToInternmentParenteralPlanBo(parenteralPlan, institutionId, episodeId, SourceType.EMERGENCY_CARE));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/parenteral-plans")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<ParenteralPlanDto>> getEmergencyCareEpisodeParenteralPlans(@PathVariable(name = "institutionId") Integer institutionId,
																						  @PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<ParenteralPlanDto> result = parenteralPlanService.getEpisodeParenteralPlans(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output => {}", result.toString());
		return ResponseEntity.ok(result);
	}

}
