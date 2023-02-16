package net.pladema.medicalconsultation.equipmentdiary.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;

import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EditEquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryEmptyAppointmentsValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.EquipmentDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.ExistingEquipmentDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.equipmentdiary.controller.constraints.ValidEquipmentDiary;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.CompleteEquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryADto;

import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentDiaryOpeningHoursDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.dto.EquipmentOpeningHoursDto;
import net.pladema.medicalconsultation.equipmentdiary.controller.mapper.EquipmentDiaryMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryBoMapper;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.EquipmentDiaryBo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

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

	private final AppointmentService appointmentService;

	public EquipmentDiaryController(
			EquipmentDiaryMapper equipmentDiaryMapper,
			EquipmentDiaryService equipmentDiaryService,
			LocalDateMapper localDateMapper,
			FeatureFlagsService featureFlagsService,
			EquipmentDiaryBoMapper equipmentDiaryBoMapper,
			AppointmentService appointmentService
	) {
		this.equipmentDiaryMapper = equipmentDiaryMapper;
		this.equipmentDiaryService = equipmentDiaryService;
		this.localDateMapper = localDateMapper;
		this.featureFlagsService = featureFlagsService;
		this.equipmentDiaryBoMapper = equipmentDiaryBoMapper;
		this.appointmentService = appointmentService;
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

	@GetMapping("/{equipmentDiaryId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<CompleteEquipmentDiaryDto> getDiary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@ValidEquipmentDiary @PathVariable(name = "equipmentDiaryId") Integer equipmentDiaryId) {
		log.debug("Input parameters -> institutionId {}, equipmentDiaryId {}", institutionId, equipmentDiaryId);
        Optional<CompleteEquipmentDiaryBo> completeEquipmnetDiaryBo = equipmentDiaryService.getEquipmentDiary(equipmentDiaryId);
		CompleteEquipmentDiaryDto result = completeEquipmnetDiaryBo.map(equipmentDiaryMapper::toCompleteEquipmentDiaryDto).orElse(new CompleteEquipmentDiaryDto());
        log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{equipmentDiaryId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	@Transactional
	public ResponseEntity<Integer> updateDiary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@ValidEquipmentDiary @PathVariable(name = "equipmentDiaryId") Integer equipmentDiaryId,
			@RequestBody @Valid @ExistingEquipmentDiaryPeriodValid @EditEquipmentDiaryOpeningHoursValid
			@EquipmentDiaryEmptyAppointmentsValid EquipmentDiaryDto equipmentDiaryDto) throws DiaryException {
		log.debug("Input parameters -> equipmentDiaryDto {}", equipmentDiaryDto);
		EquipmentDiaryBo diaryToUpdate = equipmentDiaryMapper.toEquipmentDiaryBo(equipmentDiaryDto);
		diaryToUpdate.setId(equipmentDiaryId);
		updateOutOfBoundsAppointments(equipmentDiaryDto);
		Integer result = equipmentDiaryService.updateDiary(diaryToUpdate);
		return ResponseEntity.ok().body(result);
	}

	private void updateOutOfBoundsAppointments(EquipmentDiaryDto diaryToUpdate) {
		Collection<AppointmentBo> appointments = appointmentService.getFutureActiveAppointmentsByEquipmentDiary(diaryToUpdate.getId());

		LocalDate from = localDateMapper.fromStringToLocalDate(diaryToUpdate.getStartDate());
		LocalDate to = localDateMapper.fromStringToLocalDate(diaryToUpdate.getEndDate());

		HashMap<Short, List<EquipmentDiaryOpeningHoursDto>> appointmentsByWeekday = diaryToUpdate.getEquipmentDiaryOpeningHours().stream()
				.collect(groupingBy(edoh -> edoh.getOpeningHours().getDayWeekId(),
						HashMap<Short, List<EquipmentDiaryOpeningHoursDto>>::new, Collectors.toList()));

		appointments.stream().filter(a -> {
			List<EquipmentDiaryOpeningHoursDto> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
			return newHours == null
					|| outOfDiaryBounds(from, to, a)
					|| outOfOpeningHoursBounds(a, newHours);
		}).forEach(
				this::changeToOutOfDiaryState
		);
	}
	
	private boolean outOfDiaryBounds(LocalDate from, LocalDate to, AppointmentBo a) {
		return !isBetween(from, to, a);
	}

	private boolean isBetween(LocalDate from, LocalDate to, AppointmentBo a) {
		return a.getDate().compareTo(from)>=0 && a.getDate().compareTo(to)<=0;
	}

	private boolean outOfOpeningHoursBounds(AppointmentBo a, List<EquipmentDiaryOpeningHoursDto> newHours) {
		return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getOpeningHours()) && sameMedicalAttention(a, newOH));
	}

	private boolean fitsIn(AppointmentBo appointment, EquipmentOpeningHoursDto openingHours) {
		LocalTime from = localDateMapper.fromStringToLocalTime(openingHours.getFrom());
		LocalTime to = localDateMapper.fromStringToLocalTime(openingHours.getTo());
		return (appointment.getHour().equals(from) || appointment.getHour().isAfter(from)) && appointment.getHour().isBefore(to);
	}

	private boolean sameMedicalAttention(AppointmentBo a, EquipmentDiaryOpeningHoursDto newOH) {
		return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
	}
	private void changeToOutOfDiaryState(AppointmentBo appointmentBo) {
		if(!appointmentBo.getDate().isAfter(LocalDate.now()))
			return;

		if(appointmentBo.getAppointmentStateId() != AppointmentState.BLOCKED)
			appointmentService.updateState(appointmentBo.getId(), AppointmentState.OUT_OF_DIARY, UserInfo.getCurrentAuditor(), "Fuera de agenda");
		else
			appointmentService.delete(appointmentBo);
	}
}
