package net.pladema.medicalconsultation.equipmentdiary.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


import net.pladema.medicalconsultation.diary.service.exception.DiaryException;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;

import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.mapper.EquipmentDiaryMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryBoMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/equipmentDiary")
@Tag(name = "EquipmentDiary", description = "EquipmentDiary")
@Validated
public class EquipmentDiaryController {

	public static final String OUTPUT = "Output -> {}";

	private final EquipmentDiaryMapper equipmentDiaryMapper;
	private final EquipmentDiaryService equipmentDiaryService;

	private final EquipmentDiaryBoMapper equipmentDiaryBoMapper;

	private final LocalDateMapper localDateMapper;

	private final FeatureFlagsService featureFlagsService;

	public EquipmentDiaryController(
			EquipmentDiaryMapper equipmentDiaryMapper,
			EquipmentDiaryService equipmentDiaryService,
			LocalDateMapper localDateMapper,
			FeatureFlagsService featureFlagsService,
			EquipmentDiaryBoMapper equipmentDiaryBoMapper
	) {
		this.equipmentDiaryMapper = equipmentDiaryMapper;
		this.equipmentDiaryService = equipmentDiaryService;
		this.localDateMapper = localDateMapper;
		this.featureFlagsService = featureFlagsService;
		this.equipmentDiaryBoMapper = equipmentDiaryBoMapper;
	}

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<Integer> addDiary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody @Valid @NewDiaryPeriodValid @EquipmentDiaryOpeningHoursValid EquipmentDiaryADto  equipmentDiaryADto) throws DiaryException {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		log.debug("Input parameters -> diaryADto {}", equipmentDiaryADto);
		EquipmentDiaryBo equipmentdiaryToSave = equipmentDiaryMapper.toEquipmentDiaryBo(equipmentDiaryADto);
		Integer result = equipmentDiaryService.addDiary(equipmentdiaryToSave);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
	@GetMapping("/equipment/{equipmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<EquipmentDiaryDto>> getEquipmentDiariesFromEquipment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "equipmentId") Integer equipmentId){
		List<EquipmentDiaryBo> equipmentDiariesBOActive = equipmentDiaryService.getEquipmentDiariesFromEquipment(equipmentId, true);
		List<EquipmentDiaryDto> result = equipmentDiaryBoMapper.toListEquipmentDiaryDto(equipmentDiariesBOActive);
		return ResponseEntity.ok().body(result);
	}

}
