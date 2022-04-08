package net.pladema.clinichistory.hospitalization.controller.documents.indication;

import java.util.List;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

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
		Integer result = internmentDietService.addDiet(mapToBo(dietDto,institutionId,internmentEpisodeId));
		log.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}

	private InternmentDietBo mapToBo(DietDto dto, Integer institutionId, Integer internmentEpisodeId) {
		InternmentDietBo result = new InternmentDietBo();
		result.setDescription(dto.getDescription());
		result.setPatientId(dto.getPatientId());
		result.setTypeId(dto.getType().getId());
		result.setProfessionalId(dto.getProfessionalId());
		result.setStatusId(dto.getStatus().getId());
		result.setIndicationDate(localDateMapper.fromDateDto(dto.getIndicationDate()));
		result.setInstitutionId(institutionId);
		result.setEncounterId(internmentEpisodeId);
		return result;
	}


}
