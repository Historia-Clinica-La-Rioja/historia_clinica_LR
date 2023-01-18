package net.pladema.medicalconsultation.equipmentdiary.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


import net.pladema.medicalconsultation.diary.service.exception.DiaryException;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;

import net.pladema.medicalconsultation.equipmentdiary.controller.mapper.EquipmetDiaryMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmetDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import net.pladema.staff.service.HealthcareProfessionalService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/equipmentDiary")
@Tag(name = "EquipmentDiary", description = "EquipmentDiary")
@Validated
public class EquipmentDiaryController {

	public static final String OUTPUT = "Output -> {}";

	private final EquipmetDiaryMapper equipmetDiaryMapper;
	private final EquipmetDiaryService  equipmetDiaryService;

	private final LocalDateMapper localDateMapper;

	private final FeatureFlagsService featureFlagsService;

	public EquipmentDiaryController(
			EquipmetDiaryMapper equipmetDiaryMapper,
			EquipmetDiaryService  equipmetDiaryService,
			LocalDateMapper localDateMapper,
			FeatureFlagsService featureFlagsService
	) {
		this.equipmetDiaryMapper = equipmetDiaryMapper;
		this.equipmetDiaryService = equipmetDiaryService;
		this.localDateMapper = localDateMapper;
		this.featureFlagsService = featureFlagsService;
	}

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<Integer> addDiary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody @Valid @NewDiaryPeriodValid EquipmentDiaryADto  equipmentDiaryADto) throws DiaryException {
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES))
			return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
		log.debug("Input parameters -> diaryADto {}", equipmentDiaryADto);
		EquipmentDiaryBo equipmentdiaryToSave = equipmetDiaryMapper.toEquipmentDiaryBo(equipmentDiaryADto);
		Integer result = equipmetDiaryService.addDiary(equipmentdiaryToSave);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}
}
