package net.pladema.medicalconsultation.diary.controller;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import net.pladema.medicalconsultation.diary.controller.constraints.DiaryEmptyAppointmentsValid;
import net.pladema.medicalconsultation.diary.controller.constraints.EditDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.constraints.ExistingDiaryPeriodValid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryDeleteableAppoinmentsValid;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.constraints.NewDiaryPeriodValid;
import net.pladema.medicalconsultation.diary.controller.constraints.ValidDiary;
import net.pladema.medicalconsultation.diary.controller.constraints.ValidDiaryProfessionalId;
import net.pladema.medicalconsultation.diary.controller.dto.BlockDto;
import net.pladema.medicalconsultation.diary.controller.dto.CompleteDiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryADto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryListDto;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.mapper.DiaryMapper;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/diary")
@Tag(name = "Diary", description = "Diary")
@Validated
public class DiaryController {

    public static final String OUTPUT = "Output -> {}";

    private final DiaryMapper diaryMapper;

    private final DiaryService diaryService;

    private final CreateAppointmentService createAppointmentService;

    private final AppointmentService appointmentService;

    private final LocalDateMapper localDateMapper;

    public DiaryController(
            DiaryMapper diaryMapper,
            DiaryService diaryService,
            CreateAppointmentService createAppointmentService,
            AppointmentService appointmentService,
            LocalDateMapper localDateMapper
    ) {
        this.diaryMapper = diaryMapper;
        this.diaryService = diaryService;
        this.createAppointmentService = createAppointmentService;
        this.appointmentService = appointmentService;
        this.localDateMapper = localDateMapper;
    }

    @GetMapping("/{diaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<CompleteDiaryDto> getDiary(@PathVariable(name = "institutionId") Integer institutionId,
            @ValidDiary @PathVariable(name = "diaryId") Integer diaryId) {
        log.debug("Input parameters -> institutionId {}, diaryId {}", institutionId, diaryId);
        Optional<CompleteDiaryBo> diaryBo = diaryService.getDiary(diaryId);
        CompleteDiaryDto result = diaryBo.map(diaryMapper::toCompleteDiaryDto).orElse(new CompleteDiaryDto());
        log.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    @Transactional
    public ResponseEntity<Integer> addDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody @Valid @NewDiaryPeriodValid @DiaryOpeningHoursValid DiaryADto diaryADto) {
        log.debug("Input parameters -> diaryADto {}", diaryADto);
        DiaryBo diaryToSave = diaryMapper.toDiaryBo(diaryADto);
        Integer result = diaryService.addDiary(diaryToSave);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
    
    @PutMapping("/{diaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    @Transactional
    public ResponseEntity<Integer> updateDiary(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidDiary @PathVariable(name = "diaryId") Integer diaryId,
            @RequestBody @Valid @ExistingDiaryPeriodValid @EditDiaryOpeningHoursValid @DiaryEmptyAppointmentsValid DiaryDto diaryDto) {
        log.debug("Input parameters -> diaryADto {}", diaryDto);
        DiaryBo diaryToUpdate = diaryMapper.toDiaryBo(diaryDto);
        diaryToUpdate.setId(diaryId);
        updateOutOfBoundsAppointments(diaryDto);
        Integer result = diaryService.updateDiary(diaryToUpdate);
        return ResponseEntity.ok().body(result);
    }

    private void updateOutOfBoundsAppointments(DiaryDto diaryToUpdate) {
        Collection<AppointmentBo> appointments = appointmentService.getFutureActiveAppointmentsByDiary(diaryToUpdate.getId());

        LocalDate from = localDateMapper.fromStringToLocalDate(diaryToUpdate.getStartDate());
        LocalDate to = localDateMapper.fromStringToLocalDate(diaryToUpdate.getEndDate());

        HashMap<Short, List<DiaryOpeningHoursDto>> appointmentsByWeekday = diaryToUpdate.getDiaryOpeningHours().stream()
                .collect(groupingBy(doh -> doh.getOpeningHours().getDayWeekId(),
                        HashMap<Short, List<DiaryOpeningHoursDto>>::new, Collectors.toList()));

        appointments.stream().filter(a -> {
            List<DiaryOpeningHoursDto> newHours = appointmentsByWeekday.get(getWeekDay(a.getDate()));
            return newHours == null
                    || outOfDiaryBounds(from, to, a)
                    || outOfOpeningHoursBounds(a, newHours);
        }).forEach(
                this::changeToOutOfDiaryState
        );
    }

    private void changeToOutOfDiaryState(AppointmentBo appointmentBo) {
        if(!appointmentBo.getDate().isAfter(LocalDate.now()))
            return;

        if(appointmentBo.getAppointmentStateId() != AppointmentState.BLOCKED)
            appointmentService.updateState(appointmentBo.getId(), AppointmentState.OUT_OF_DIARY, UserInfo.getCurrentAuditor(), "Fuera de agenda");
        else
            appointmentService.delete(appointmentBo);
    }

    private boolean outOfOpeningHoursBounds(AppointmentBo a, List<DiaryOpeningHoursDto> newHours) {
        return newHours.stream().noneMatch(newOH -> fitsIn(a, newOH.getOpeningHours()) && sameMedicalAttention(a, newOH));
    }

    private boolean sameMedicalAttention(AppointmentBo a, DiaryOpeningHoursDto newOH) {
        return newOH.getMedicalAttentionTypeId().equals(a.getMedicalAttentionTypeId());
    }

    private boolean outOfDiaryBounds(LocalDate from, LocalDate to, AppointmentBo a) {
        return !isBetween(from, to, a);
    }

    private boolean isBetween(LocalDate from, LocalDate to, AppointmentBo a) {
        return a.getDate().compareTo(from)>=0 && a.getDate().compareTo(to)<=0;
    }

    private boolean fitsIn(AppointmentBo appointment, OpeningHoursDto openingHours) {
        LocalTime from = localDateMapper.fromStringToLocalTime(openingHours.getFrom());
        LocalTime to = localDateMapper.fromStringToLocalTime(openingHours.getTo());
        return (appointment.getHour().equals(from) || appointment.getHour().isAfter(from)) && appointment.getHour().isBefore(to);
    }

    
    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
    @ValidDiaryProfessionalId
    public ResponseEntity<Collection<DiaryListDto>> getDiaries(@PathVariable(name = "institutionId")  Integer institutionId,
                                                               @RequestParam(name = "healthcareProfessionalId") Integer healthcareProfessionalId,
                                                               @RequestParam(name = "specialtyId", required = false) Integer specialtyId){
        log.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}, specialtyId{}", institutionId, healthcareProfessionalId, specialtyId);
        Collection<DiaryBo> diaryBos = diaryService.getActiveDiariesBy(healthcareProfessionalId, specialtyId, institutionId);
        Collection<DiaryListDto> result = diaryMapper.toCollectionDiaryListDto(diaryBos);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{diaryId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "institutionId") Integer institutionId,
                                                     @PathVariable(name = "diaryId") @DiaryDeleteableAppoinmentsValid Integer diaryId) {
        log.debug("Input parameters -> institutionId {}, diaryId {}", institutionId, diaryId);
        diaryService.deleteDiary(diaryId);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @PostMapping("/{diaryId}/block")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<Boolean> block(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "diaryId") Integer diaryId,
			@RequestBody BlockDto blockDto) {
		log.debug("Input parameters -> institutionId {}, diaryId {}, blockDto {}", institutionId, diaryId, blockDto);
		DiaryBo diaryBo = diaryService.getDiary(diaryId).orElseThrow();

		LocalDate startingBlockingDate = localDateMapper.fromDateDto(blockDto.getInitDateDto());
		LocalDate endingBlockingDate = localDateMapper.fromDateDto(blockDto.getEndDateDto());

		List<LocalDate> blockedDates = startingBlockingDate.datesUntil(endingBlockingDate).collect(Collectors.toList());
		blockedDates.add(endingBlockingDate);
		blockedDates = blockedDates.stream().filter(potentialBlockedDay -> diaryBo.getDiaryOpeningHours()
				.stream().anyMatch(diaryOpeningHours -> dayIsIncludedInOpeningHours(potentialBlockedDay, diaryOpeningHours)))
				.collect(Collectors.toList());

		List<AppointmentBo> listAppointments = new ArrayList<>();

		if (blockDto.isFullBlock())
			completeDiaryBlock(blockDto, diaryBo, blockedDates, listAppointments);
		else
			blockedDates.forEach(date -> generateBlockInterval(diaryBo, listAppointments, date, blockDto));

		assertNoAppointments(diaryId, listAppointments);

		listAppointments.forEach(createAppointmentService::execute);

		return ResponseEntity.ok(Boolean.TRUE);
	}

	private void completeDiaryBlock(BlockDto blockDto, DiaryBo diaryBo, List<LocalDate> blockedDates, List<AppointmentBo> listAppointments) {
		blockedDates.forEach(blockedDate -> {
			List<DiaryOpeningHoursBo> relatedOpeningHours = diaryBo.getDiaryOpeningHours().stream()
					.filter(diaryOpeningHours -> dayIsIncludedInOpeningHours(blockedDate, diaryOpeningHours)).collect(Collectors.toList());
			relatedOpeningHours.forEach(openingHours -> {
				BlockDto a = new BlockDto(localDateMapper.toDateDto(blockedDate), localDateMapper.toDateDto(blockedDate),
						localDateMapper.toTimeDto(openingHours.getOpeningHours().getFrom()), localDateMapper.toTimeDto(openingHours.getOpeningHours().getTo()),
						blockDto.getAppointmentBlockMotiveId());
				generateBlockInterval(diaryBo, listAppointments, blockedDate, a);
			});
		});
	}

	private void generateBlockInterval(DiaryBo diaryBo, List<AppointmentBo> listAppointments, LocalDate blockedDate, BlockDto a) {
		listAppointments.addAll(getSlots(a, diaryBo).stream().map(slot -> mapTo(blockedDate, diaryBo, slot, a)).collect(Collectors.toList()));
	}

	@GetMapping("/hasActiveDiaries/{healthcareProfessionalId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<Boolean> hasActiveDiaries(@PathVariable(name = "institutionId") Integer institutionId,
													@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId) {
		log.debug("Input parameters -> institutionId {}, healthcareProfessionalId {}", institutionId, healthcareProfessionalId);

		Boolean result = diaryService.hasActiveDiariesInInstitution(healthcareProfessionalId, institutionId);

		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/{diaryId}/unblock")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<Boolean> unblock(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "diaryId") Integer diaryId,
			@RequestBody BlockDto unblockDto) {
		log.debug("Unblock -> Input parameters -> institutionId {}, diaryId {}, blockDto {}", institutionId, diaryId, unblockDto);
		DiaryBo diaryBo = diaryService.getDiary(diaryId).orElseThrow();

		LocalDate startingBlockingDate = localDateMapper.fromDateDto(unblockDto.getInitDateDto());
		LocalDate endingBlockingDate = localDateMapper.fromDateDto(unblockDto.getEndDateDto());

		List<LocalDate> blockedDates = startingBlockingDate.datesUntil(endingBlockingDate).collect(Collectors.toList());
		blockedDates.add(endingBlockingDate);

		List<AppointmentBo> listAppointments = new ArrayList<>();

		if (unblockDto.isFullBlock())
			completeDiaryUnblock(unblockDto, diaryBo, blockedDates, listAppointments);
		else
			blockedDates.forEach(date -> generateUnblockInterval(diaryBo, listAppointments, date, unblockDto));

		listAppointments.forEach(appointmentService::delete);

		return ResponseEntity.ok(Boolean.TRUE);
	}

	private void completeDiaryUnblock(BlockDto unblockDto, DiaryBo diaryBo, List<LocalDate> blockedDates, List<AppointmentBo> listAppointments) {
		blockedDates.forEach(blockedDate -> {
			List<DiaryOpeningHoursBo> relatedOpeningHours = diaryBo.getDiaryOpeningHours().stream()
					.filter(diaryOpeningHours -> dayIsIncludedInOpeningHours(blockedDate, diaryOpeningHours)).collect(Collectors.toList());
			relatedOpeningHours.forEach(openingHours -> {
				BlockDto a = new BlockDto(localDateMapper.toDateDto(blockedDate), localDateMapper.toDateDto(blockedDate),
						localDateMapper.toTimeDto(openingHours.getOpeningHours().getFrom()), localDateMapper.toTimeDto(openingHours.getOpeningHours().getTo()),
						unblockDto.getAppointmentBlockMotiveId());
				generateUnblockInterval(diaryBo, listAppointments, blockedDate, a);
			});
		});
	}

	private void generateUnblockInterval(DiaryBo diaryBo, List<AppointmentBo> listAppointments, LocalDate blockedDate, BlockDto a) {
		listAppointments.addAll(getSlots(a, diaryBo).stream().map(slot -> findAppointment(blockedDate, diaryBo, slot))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(this::isBlocked)
				.collect(Collectors.toList()));
	}

	private Optional<AppointmentBo> findAppointment(LocalDate date, DiaryBo diaryBo, LocalTime slot) {
		return appointmentService.findAppointmentBy(diaryBo.getId(),
				LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth()),
				slot);
	}

	private List<LocalTime> getSlots(BlockDto blockDto, DiaryBo diaryBo) {
		var appointmentDuration = diaryBo.getAppointmentDuration();
		var localTimeInit = LocalTime.of(blockDto.getInit().getHours(), blockDto.getInit().getMinutes());
		var localTimeEnd = LocalTime.of(blockDto.getEnd().getHours(), blockDto.getEnd().getMinutes());

		assertTimeLimits(localTimeInit, localTimeEnd, appointmentDuration);

		var slots = Stream.iterate(localTimeInit, d -> d.plusMinutes(appointmentDuration))
				.limit(ChronoUnit.MINUTES.between(localTimeInit, localTimeEnd) / appointmentDuration)
				.collect(Collectors.toList());

		if (localTimeEnd.getHour() == 23 && localTimeEnd.getMinute() == 59) {
			var lastTime = slots.get(slots.size()-1).plusMinutes(appointmentDuration);
			slots.add(lastTime);
		}

		return slots;
	}

	private boolean isBlocked(AppointmentBo ap) {
		return appointmentService.getAppointment(ap.getId())
				.map(appointmentBo -> appointmentBo.getAppointmentStateId()
						.equals(AppointmentState.BLOCKED)).orElse(false);
	}

	private void assertTimeLimits(LocalTime localTimeInit, LocalTime localTimeEnd, Short appointmentDuration) {
    	if (localTimeEnd.isBefore(localTimeInit) || localTimeEnd.equals(localTimeInit))
    		throw new ConstraintViolationException("La segunda hora seleccionada debe ser posterior a la primera.",
					new HashSet(Collections.singleton("La segunda hora seleccionada debe ser posterior a la primera.")));

		if(localTimeInit.getMinute() % appointmentDuration != 0)
			throw new ConstraintViolationException("La hora de inicio no es múltiplo de la duración del turno.",
					new HashSet(Collections.singleton("La hora de inicio no es múltiplo de la duración del turno.")));

		if(localTimeEnd.getMinute() % appointmentDuration != 0 && (localTimeEnd.getMinute() != 59 && localTimeEnd.getHour() != 23))
			throw new ConstraintViolationException("La hora de fin no es múltiplo de la duración del turno.",
					new HashSet(Collections.singleton("La hora de fin no es múltiplo de la duración del turno.")));
	}

	private void assertNoAppointments(Integer diaryId, List<AppointmentBo> listAppointments) {
		if(listAppointments.stream().anyMatch(appointmentBo ->
				appointmentService.existAppointment(diaryId,
						appointmentBo.getOpeningHoursId(),
						appointmentBo.getDate(),
						appointmentBo.getHour()
				)
		)) throw new ConstraintViolationException("Algún horario de la franja horaria seleccionada tiene un turno o ya está bloqueado.",
				new HashSet(Collections.singleton("Algún horario de la franja horaria seleccionada tiene un turno o ya está bloqueado.")));
	}

	private AppointmentBo mapTo(LocalDate date, DiaryBo diaryBo, LocalTime hour, BlockDto blockDto) {
		var openingHours = diaryBo.getDiaryOpeningHours();
		AppointmentBo appointmentBo = new AppointmentBo();
		appointmentBo.setDiaryId(diaryBo.getId());
		appointmentBo.setDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth()));
		appointmentBo.setHour(hour);
		appointmentBo.setAppointmentStateId(AppointmentState.BLOCKED);
		appointmentBo.setOverturn(false);
		appointmentBo.setOpeningHoursId(getOpeningHourId(openingHours, date, blockDto).getOpeningHours().getId());
		appointmentBo.setAppointmentBlockMotiveId(blockDto.getAppointmentBlockMotiveId());
		return appointmentBo;
	}

	private DiaryOpeningHoursBo getOpeningHourId(List<DiaryOpeningHoursBo> openingHours, LocalDate date, BlockDto blockDto) {
		var dayOfWeek =
				(short)LocalDate.of(date.getYear(),
						date.getMonth(),
						date.getDayOfMonth()).getDayOfWeek().getValue();
		var localTimeInit = LocalTime.of(blockDto.getInit().getHours(), blockDto.getInit().getMinutes());
		var localTimeEnd = LocalTime.of(blockDto.getEnd().getHours(), blockDto.getEnd().getMinutes());

		return openingHours.stream()
				.filter(oh -> oh.getOpeningHours().getDayWeekId().equals(dayOfWeek))
				.filter(oh -> (oh.getOpeningHours().getFrom().isBefore(localTimeInit) || oh.getOpeningHours().getFrom().equals(localTimeInit)) &&
						(oh.getOpeningHours().getTo().isAfter(localTimeEnd) || oh.getOpeningHours().getTo().equals(localTimeEnd)))
				.findFirst().orElseThrow((() -> new ConstraintViolationException("Los horarios de inicio y fin deben pertenecer al mismo período de la agenda.",
				new HashSet(Collections.singleton("Los horarios de inicio y fin deben pertenecer al mismo período de la agenda.")))));
	}

	private boolean dayIsIncludedInOpeningHours(LocalDate date, DiaryOpeningHoursBo diaryOpeningHours) {
		final int SUNDAY_DB_VALUE = 0;
		if (date.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue())
			return diaryOpeningHours.getOpeningHours().getDayWeekId() == SUNDAY_DB_VALUE;
		return diaryOpeningHours.getOpeningHours().getDayWeekId() == date.getDayOfWeek().getValue();
	}

}
