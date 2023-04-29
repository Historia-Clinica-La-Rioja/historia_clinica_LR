package net.pladema.clinichistory.hospitalization.controller.documents.indication;

import java.util.List;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;

import net.pladema.clinichistory.hospitalization.controller.mapper.IndicationMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.indication.diet.InternmentDietService;
import net.pladema.clinichistory.hospitalization.service.indication.otherindication.InternmentOtherIndicationService;
import net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.InternmentParenteralPlanService;
import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.InternmentPharmacoService;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}")
@Tag(name = "Indication", description = "Indication")
@Validated
@Slf4j
@RequiredArgsConstructor
public class InternmentIndicationController {

	private final InternmentDietService internmentDietService;

	private final InternmentOtherIndicationService otherIndicationService;

	private final InternmentPharmacoService internmentPharmacoService;

	private final InternmentParenteralPlanService internmentParenteralPlanService;

	private final IndicationMapper indicationMapper;

	@GetMapping("/diets")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<List<DietDto>> getInternmentEpisodeDiets(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
		List<DietDto> result = internmentDietService.getInternmentEpisodeDiets(internmentEpisodeId, SourceType.HOSPITALIZATION);
		log.debug("Get active internment episode diets => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/diets/{dietId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<DietDto> getInternmentEpisodeDiet(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @PathVariable(name = "dietId") Integer dietId) {
		log.debug("Input parameters -> institutionId {}, intermentEpisodeId {}, dietId {}", institutionId, internmentEpisodeId, dietId);
		DietDto result = internmentDietService.getInternmentEpisodeDiet(dietId);
		log.debug("Get active internment episode diet by id => {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/diet")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Integer> addDiet(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @RequestBody DietDto dietDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, dietDto {}", institutionId, internmentEpisodeId, dietDto);
		Integer result = internmentDietService.addDiet(indicationMapper.mapToDietBo(dietDto,institutionId,internmentEpisodeId), SourceType.HOSPITALIZATION);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/other-indication")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Integer> addOtherIndication(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @RequestBody OtherIndicationDto otherIndicationDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, otherIndicationDto {}", institutionId, internmentEpisodeId, otherIndicationDto);
		Integer result = otherIndicationService.add(indicationMapper.mapToOtherIndicationBo(otherIndicationDto,institutionId,internmentEpisodeId), SourceType.HOSPITALIZATION);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/other-indications")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<List<OtherIndicationDto>> getInternmentEpisodeOtherIndications(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
		List<OtherIndicationDto> result = otherIndicationService.getInternmentEpisodeOtherIndications(internmentEpisodeId, SourceType.HOSPITALIZATION);
		log.debug("Get active internment episode other indications => {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/other-indications/{otherIndicationId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<OtherIndicationDto> getInternmentEpisodeOtherIndication(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @PathVariable(name = "otherIndicationId") Integer otherIndicationId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, otherIndicationId {}", institutionId, internmentEpisodeId, otherIndicationId);
		OtherIndicationDto result = otherIndicationService.getInternmentEpisodeOtherIndication(otherIndicationId);
		log.debug("Get active internment episode other indication by id => {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/pharmaco")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> addPharmaco(@PathVariable(name = "institutionId") Integer institutionId,
											   @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
											   @RequestBody PharmacoDto pharmacoDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, pharmacoDto {}", institutionId, internmentEpisodeId, pharmacoDto);
		Integer result = internmentPharmacoService.add(indicationMapper.mapToPharmacoBo(pharmacoDto, institutionId, internmentEpisodeId), SourceType.HOSPITALIZATION);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/parenteral-plan")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Integer> addParenteralPlan(@PathVariable(name = "institutionId") Integer institutionId,
													 @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
													 @RequestBody ParenteralPlanDto parenteralPlan) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, parenteralPlanDto {}", institutionId, internmentEpisodeId, parenteralPlan);
		Integer result = internmentParenteralPlanService.add(indicationMapper.mapToInternmentParenteralPlanBo(parenteralPlan, institutionId, internmentEpisodeId), SourceType.HOSPITALIZATION);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pharmacos")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<List<PharmacoSummaryDto>> getInternmentEpisodePharmacos(@PathVariable(name = "institutionId") Integer institutionId,
																				  @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
		List<PharmacoSummaryDto> result = internmentPharmacoService.getInternmentEpisodePharmacos(internmentEpisodeId, SourceType.HOSPITALIZATION);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pharmacos/{pharmacoId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<PharmacoDto> getInternmentEpisodePharmaco(@PathVariable(name = "institutionId") Integer institutionId,
																		   @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
																		   @PathVariable(name = "pharmacoId") Integer pharmacoId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, pharmacoId {}", institutionId, internmentEpisodeId, pharmacoId);
		PharmacoDto result = internmentPharmacoService.getInternmentEpisodePharmaco(pharmacoId);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/parenteral-plans")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<List<ParenteralPlanDto>> getInternmentEpisodeParenteralPlans(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
		List<ParenteralPlanDto> result = internmentParenteralPlanService.getInternmentEpisodeParenteralPlans(internmentEpisodeId, SourceType.HOSPITALIZATION);
		log.debug("Output => {}", result.toString());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/parenteral-plans/{parenteralPlanId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD, ENFERMERO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<ParenteralPlanDto> getInternmentEpisodeParenteralPlan(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @PathVariable(name = "parenteralPlanId") Integer parenteralPlanId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, parenteralPlanId {}", institutionId, internmentEpisodeId, parenteralPlanId);
		ParenteralPlanDto result = internmentParenteralPlanService.getInternmentEpisodeParenteralPlan(parenteralPlanId);
		log.debug("Output => {}", result.toString());
		return ResponseEntity.ok(result);
	}

}
