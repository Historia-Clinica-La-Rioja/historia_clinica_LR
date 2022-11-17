package net.pladema.medicalconsultation.diary.controller;

import static ar.lamansys.sgx.shared.dates.utils.DateUtils.getWeekDay;
import static java.util.stream.Collectors.groupingBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import net.pladema.medicalconsultation.appointment.controller.dto.EmptyAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.diary.controller.constraints.DiaryEmptyAppointmentsValid;
import net.pladema.medicalconsultation.diary.controller.constraints.EditDiaryOpeningHoursValid;
import net.pladema.medicalconsultation.diary.controller.constraints.ExistingDiaryPeriodValid;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentSearchDto;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;
import net.pladema.staff.service.HealthcareProfessionalService;

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

	private final HealthcareProfessionalService healthcareProfessionalService;

    private final LocalDateMapper localDateMapper;

	private final AppointmentMapper appointmentMapper;

    public DiaryController(
            DiaryMapper diaryMapper,
            DiaryService diaryService,
            CreateAppointmentService createAppointmentService,
            AppointmentService appointmentService,
			HealthcareProfessionalService healthcareProfessionalService,
            LocalDateMapper localDateMapper,
			AppointmentMapper appointmentMapper
    ) {
        this.diaryMapper = diaryMapper;
        this.diaryService = diaryService;
        this.createAppointmentService = createAppointmentService;
        this.appointmentService = appointmentService;
		this.healthcareProfessionalService = healthcareProfessionalService;
        this.localDateMapper = localDateMapper;
		this.appointmentMapper = appointmentMapper;
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
		Integer currentUserHealthcareProfessionalId = healthcareProfessionalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Collection<DiaryBo> diaryBos = diaryService.getActiveDiariesBy(currentUserHealthcareProfessionalId, healthcareProfessionalId, specialtyId, institutionId);
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

		BlockBo block = appointmentMapper.toBlockBo(blockDto);

		List<AppointmentBo> listAppointments = appointmentService.generateBlockedAppointments(diaryId, block, diaryBo, startingBlockingDate, endingBlockingDate);

		listAppointments.forEach(createAppointmentService::execute);

		return ResponseEntity.ok(Boolean.TRUE);
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

		BlockBo unblock = appointmentMapper.toBlockBo(unblockDto);

		List<AppointmentBo> listAppointments = appointmentService.unblockAppointments(unblock, diaryBo, startingBlockingDate, endingBlockingDate);

		listAppointments.forEach(appointmentService::delete);

		return ResponseEntity.ok(Boolean.TRUE);
	}

	@GetMapping("/active-diaries-alias")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA, ADMINISTRATIVO')")
	public ResponseEntity<List<String>> getManyByActiveDiariesAndProfessionals(
			@PathVariable(name = "institutionId") Integer institutionId) {
		List<String> activeDiariesAliases = diaryService.getActiveDiariesAliases(institutionId);
		log.debug("Get all aliases by active diaries and Institution {} ", institutionId);
		return ResponseEntity.ok(activeDiariesAliases);
	}

	@PostMapping("/generate-empty-appointments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_AGENDA, ADMINISTRATIVO')")
	public ResponseEntity<List<EmptyAppointmentDto>> getAvailableAppointments(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody AppointmentSearchDto searchCriteria) {
		log.debug("Generate all empty appointments by Institution {} and criteria {} ", institutionId, searchCriteria);
		AppointmentSearchBo searchCriteriaBo = appointmentMapper.toAppointmentSearchBo(searchCriteria);
		List<EmptyAppointmentBo> emptyAppointments = diaryService.getEmptyAppointmentsBySearchCriteria(institutionId, searchCriteriaBo);
		List<EmptyAppointmentDto> result = emptyAppointments.stream().map(appointmentMapper::toEmptyAppointmentDto).collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

}
