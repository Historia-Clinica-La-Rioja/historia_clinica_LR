package net.pladema.clinichistory.hospitalization.controller.documents.indication;

import java.time.LocalDateTime;
import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.IndicationDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.OtherIndicationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentIndicationBo;
import net.pladema.clinichistory.hospitalization.service.indication.otherindication.InternmentOtherIndicationService;

import net.pladema.clinichistory.hospitalization.service.indication.otherindication.domain.InternmentOtherIndicationBo;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.indication.diet.InternmentDietService;
import net.pladema.clinichistory.hospitalization.service.indication.diet.domain.InternmentDietBo;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}")
@Tag(name = "Indication", description = "Indication")
@Validated
@Slf4j
@RequiredArgsConstructor
public class InternmentIndicationController {

	private final InternmentDietService internmentDietService;

	private final InternmentOtherIndicationService otherIndicationService;

	private final LocalDateMapper localDateMapper;

	@GetMapping("/diets")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<DietDto>> getInternmentEpisodeDiets(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
		List<DietDto> result = internmentDietService.getInternmentEpisodeDiets(internmentEpisodeId);
		log.debug("Get active internment episode diets => {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/diet")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Integer> addDiet(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @RequestBody DietDto dietDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, dietDto {}", institutionId, internmentEpisodeId, dietDto);
		Integer result = internmentDietService.addDiet(mapToDietBo(dietDto,institutionId,internmentEpisodeId));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/other-indication")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Integer> addOtherIndication(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId, @RequestBody OtherIndicationDto otherIndicationDto) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, otherIndicationDto {}", institutionId, internmentEpisodeId, otherIndicationDto);
		Integer result = otherIndicationService.add(mapToOtherIndicationBo(otherIndicationDto,institutionId,internmentEpisodeId));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/other-indication")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<OtherIndicationDto>> getInternmentEpisodeOtherIndications(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
		List<OtherIndicationDto> result = otherIndicationService.getInternmentEpisodeOtherIndications(internmentEpisodeId);
		log.debug("Get active internment episode other indications => {}", result);
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

	private DosageBo toDosageBo(NewDosageDto dto, DateDto indicationDate){
		DosageBo result = new DosageBo();
		result.setFrequency(dto.getFrequency());
		result.setPeriodUnit(EUnitsOfTimeBo.map(dto.getPeriodUnit()));
		LocalDateTime startDate = (dto.getStartDateTime()!=null)
				? localDateMapper.fromDateTimeDto(dto.getStartDateTime())
				: localDateMapper.fromDateTimeDto(new DateTimeDto(indicationDate, new TimeDto(0,0,0)));
		result.setStartDate(startDate);
		result.setEndDate(startDate.plusDays(1).toLocalDate().atStartOfDay());
		return result;
	}
}
