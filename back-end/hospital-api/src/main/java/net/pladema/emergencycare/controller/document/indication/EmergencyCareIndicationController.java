package net.pladema.emergencycare.controller.document.indication;

import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FrequencyBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherPharmacoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.FrequencyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.IndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherPharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.PharmacoSummaryDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.indication.diet.InternmentDietService;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentDietBo;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentIndicationBo;
import net.pladema.clinichistory.hospitalization.service.indication.otherindication.InternmentOtherIndicationService;
import net.pladema.clinichistory.hospitalization.service.indication.otherindication.domain.InternmentOtherIndicationBo;
import net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.InternmentParenteralPlanService;
import net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.domain.InternmentParenteralPlanBo;
import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.InternmentPharmacoService;
import net.pladema.clinichistory.hospitalization.service.indication.pharmaco.domain.InternmentPharmacoBo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/emergency-care/episode/{episodeId}")
@Tag(name = "Emergency Care Indication", description = "Emergency Care Indication")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EmergencyCareIndicationController {

	private final InternmentDietService internmentDietService;

	private final InternmentOtherIndicationService otherIndicationService;

	private final InternmentPharmacoService internmentPharmacoService;

	private final InternmentParenteralPlanService internmentParenteralPlanService;

	private final LocalDateMapper localDateMapper;

	@GetMapping("/diets")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<DietDto>> getEmergencyCareEpisodeDiets(@PathVariable(name = "institutionId") Integer institutionId,
																	  @PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<DietDto> result = internmentDietService.getInternmentEpisodeDiets(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/diet")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<Integer> addDiet(@PathVariable(name = "institutionId") Integer institutionId,
										   @PathVariable(name = "episodeId") Integer episodeId,
										   @RequestBody DietDto dietDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, dietDto {}", institutionId, episodeId, dietDto);
		Integer result = internmentDietService.addDiet(mapToDietBo(dietDto, institutionId, episodeId), SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/other-indication")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<Integer> addOtherIndication(@PathVariable(name = "institutionId") Integer institutionId,
													  @PathVariable(name = "episodeId") Integer episodeId,
													  @RequestBody OtherIndicationDto otherIndicationDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, otherIndicationDto {}", institutionId, episodeId, otherIndicationDto);
		Integer result = otherIndicationService.add(mapToOtherIndicationBo(otherIndicationDto, institutionId, episodeId), SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/other-indications")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<OtherIndicationDto>> getEmergencyCareEpisodeOtherIndications(@PathVariable(name = "institutionId") Integer institutionId,
																							@PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<OtherIndicationDto> result = otherIndicationService.getInternmentEpisodeOtherIndications(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/pharmaco")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<Integer> addPharmaco(@PathVariable(name = "institutionId") Integer institutionId,
											   @PathVariable(name = "episodeId") Integer episodeId,
											   @RequestBody PharmacoDto pharmacoDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, pharmacoDto {}", institutionId, episodeId, pharmacoDto);
		Integer result = internmentPharmacoService.add(mapToPharmacoBo(pharmacoDto, institutionId, episodeId), SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pharmacos")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<PharmacoSummaryDto>> getEmergencyCareEpisodePharmacos(@PathVariable(name = "institutionId") Integer institutionId,
																					 @PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<PharmacoSummaryDto> result = internmentPharmacoService.getInternmentEpisodePharmacos(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/parenteral-plan")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<Integer> addParenteralPlan(@PathVariable(name = "institutionId") Integer institutionId,
													 @PathVariable(name = "episodeId") Integer episodeId,
													 @RequestBody ParenteralPlanDto parenteralPlan) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, parenteralPlanDto {}", institutionId, episodeId, parenteralPlan);
		Integer result = internmentParenteralPlanService.add(mapToInternmentParenteralPlanBo(parenteralPlan, institutionId, episodeId), SourceType.EMERGENCY_CARE);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/parenteral-plans")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<List<ParenteralPlanDto>> getEmergencyCareEpisodeParenteralPlans(@PathVariable(name = "institutionId") Integer institutionId,
																						  @PathVariable(name = "episodeId") Integer episodeId) {
		log.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		List<ParenteralPlanDto> result = internmentParenteralPlanService.getInternmentEpisodeParenteralPlans(episodeId, SourceType.EMERGENCY_CARE);
		log.debug("Output => {}", result.toString());
		return ResponseEntity.ok(result);
	}

	private InternmentDietBo mapToDietBo(DietDto dto, Integer institutionId, Integer internmentEpisodeId) {
		InternmentDietBo result = new InternmentDietBo();
		result.setDescription(dto.getDescription());
		return (InternmentDietBo) setIndicationInfoBo(dto,result,institutionId,internmentEpisodeId);
	}

	private InternmentOtherIndicationBo mapToOtherIndicationBo(OtherIndicationDto dto, Integer institutionId, Integer internmentEpisodeId) {
		InternmentOtherIndicationBo result = new InternmentOtherIndicationBo();
		result.setDescription(dto.getDescription());
		result.setOtherIndicationTypeId(dto.getOtherIndicationTypeId());
		result.setOtherType(dto.getOtherType());
		result.setDosageBo(toDosageBo(dto.getDosage(),dto.getIndicationDate()));
		return (InternmentOtherIndicationBo) setIndicationInfoBo(dto,result,institutionId,internmentEpisodeId);
	}

	private InternmentPharmacoBo mapToPharmacoBo(PharmacoDto dto, Integer institutionId, Integer internmentEpisodeId) {
		InternmentPharmacoBo result = new InternmentPharmacoBo();
		result.setSnomed(dto.getSnomed() != null? new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()) : null);
		result.setDosage(toDosageBo(dto.getDosage(),dto.getIndicationDate()));
		if (dto.getSolvent() != null)
			result.setSolvent(toOtherPharmacoBo(dto.getSolvent(), dto.getIndicationDate()));
		result.setHealthConditionId(dto.getHealthConditionId());
		result.setFoodRelationId(dto.getFoodRelationId());
		result.setPatientProvided(dto.getPatientProvided());
		result.setViaId(dto.getViaId());
		DocumentObservationsBo documentObservationsBo = new DocumentObservationsBo();
		documentObservationsBo.setOtherNote(dto.getNote());
		result.setNotes(documentObservationsBo);
		return (InternmentPharmacoBo) setIndicationInfoBo(dto, result, institutionId, internmentEpisodeId);
	}

	private InternmentIndicationBo setIndicationInfoBo (IndicationDto dto, InternmentIndicationBo bo, Integer institutionId, Integer internmentEpisodeId) {
		bo.setPatientId(dto.getPatientId());
		bo.setTypeId(dto.getType().getId());
		bo.setProfessionalId(dto.getProfessionalId());
		bo.setStatusId(dto.getStatus().getId());
		bo.setIndicationDate(localDateMapper.fromDateDto(dto.getIndicationDate()));
		bo.setInstitutionId(institutionId);
		bo.setEncounterId(internmentEpisodeId);
		return bo;
	}

	private DosageBo toDosageBo(NewDosageDto dto, DateDto indicationDate) {
		DosageBo result = new DosageBo();
		result.setFrequency(dto.getFrequency());
		if(dto.getPeriodUnit() != null)
			result.setPeriodUnit(EUnitsOfTimeBo.map(dto.getPeriodUnit()));
		LocalDateTime startDate = (dto.getStartDateTime()!=null)
				? localDateMapper.fromDateTimeDto(dto.getStartDateTime())
				: localDateMapper.fromDateTimeDto(new DateTimeDto(indicationDate, new TimeDto(0,0,0)));
		result.setStartDate(startDate);
		result.setEndDate(startDate.plusDays(1).toLocalDate().atStartOfDay());
		result.setEvent(dto.getEvent());
		if(dto.getQuantity() != null)
			result.setQuantity(new QuantityBo(dto.getQuantity().getValue(), dto.getQuantity().getUnit()));
		return result;
	}

	private OtherPharmacoBo toOtherPharmacoBo(OtherPharmacoDto dto, DateDto indicationDate) {
		OtherPharmacoBo result = new OtherPharmacoBo();
		result.setSnomed(new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()));
		result.setDosage(toDosageBo(dto.getDosage(), indicationDate));
		return result;
	}

	private InternmentParenteralPlanBo mapToInternmentParenteralPlanBo (ParenteralPlanDto dto, Integer institutionId, Integer internmentEpisodeId){
		InternmentParenteralPlanBo result = new InternmentParenteralPlanBo();
		result.setDosage(toDosageBo(dto.getDosage(), dto.getIndicationDate()));
		result.setFrequency(mapToFrequencyBo(dto.getFrequency()));
		result.setSnomed(new SnomedBo(dto.getSnomed().getSctid(), dto.getSnomed().getPt()));
		result.setVia(dto.getVia());
		result.setPharmacos(dto.getPharmacos().stream().
				map(p -> toOtherPharmacoBo(p, dto.getIndicationDate())).collect(Collectors.toList()));
		return (InternmentParenteralPlanBo) setIndicationInfoBo(dto, result, institutionId, internmentEpisodeId);
	}

	private FrequencyBo mapToFrequencyBo(FrequencyDto frequency){
		FrequencyBo result = new FrequencyBo();
		result.setFlowMlHour(frequency.getFlowMlHour());
		result.setFlowDropsHour(frequency.getFlowDropsHour());
		result.setDailyVolume(frequency.getDailyVolume());
		if (frequency.getDuration()!=null)
			result.setDuration(LocalTime.of(
					frequency.getDuration().getHours()==null ? 0 : frequency.getDuration().getHours(),
					frequency.getDuration().getMinutes()==null ? 0 : frequency.getDuration().getMinutes(),
					frequency.getDuration().getSeconds()==null ? 0 : frequency.getDuration().getSeconds()));
		return result;
	}

}
