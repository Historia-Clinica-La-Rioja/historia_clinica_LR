package net.pladema.medicalconsultation.appointment.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.UpdateAppointmentDateDto;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentDiary;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentState;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentBasicPatientDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDailyAmountDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AssignedAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.UpdateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.controller.mapper.ExternalPatientCoverageMapper;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentDailyAmountService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.notifypatient.NotifyPatient;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments")
@Tag(name = "Appointments", description = "Appointments")
@Validated
public class AppointmentsController {

    public static final String OUTPUT = "Output -> {}";

    private final AppointmentDailyAmountService appointmentDailyAmountService;

    private final AppointmentService appointmentService;

    private final AppointmentValidatorService appointmentValidatorService;

    private final CreateAppointmentService createAppointmentService;

    private final AppointmentMapper appointmentMapper;

    private final PatientExternalService patientExternalService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final DateTimeProvider dateTimeProvider;

    private final NotifyPatient notifyPatient;

    private final BookingPersonService bookingPersonService;

	private final LocalDateMapper dateMapper;

    @Value("${test.stress.disable.validation:false}")
    private boolean disableValidation;

    @Value("${habilitar.boton.consulta:false}")
    private boolean enableNewConsultation;

	private final LocalDateMapper localDateMapper;

	public AppointmentsController(
            AppointmentDailyAmountService appointmentDailyAmountService,
            AppointmentService appointmentService,
            AppointmentValidatorService appointmentValidatorService,
            CreateAppointmentService createAppointmentService,
            AppointmentMapper appointmentMapper,
            PatientExternalService patientExternalService,
            HealthcareProfessionalExternalService healthcareProfessionalExternalService,
            DateTimeProvider dateTimeProvider,
            NotifyPatient notifyPatient,
            BookingPersonService bookingPersonService,
			LocalDateMapper dateMapper,
			LocalDateMapper localDateMapper) {
        this.appointmentDailyAmountService = appointmentDailyAmountService;
        this.appointmentService = appointmentService;
        this.appointmentValidatorService = appointmentValidatorService;
        this.createAppointmentService = createAppointmentService;
        this.appointmentMapper = appointmentMapper;
        this.patientExternalService = patientExternalService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.dateTimeProvider = dateTimeProvider;
        this.notifyPatient = notifyPatient;
        this.bookingPersonService = bookingPersonService;
		this.dateMapper = dateMapper;
		this.localDateMapper = localDateMapper;
	}


    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @ValidAppointment
    public ResponseEntity<Integer> create(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody @Valid CreateAppointmentDto createAppointmentDto
    ) {
        log.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
        AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
        newAppointmentBo = createAppointmentService.execute(newAppointmentBo);
		Integer result = newAppointmentBo.getId();
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }


    @PostMapping(value = "/update")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Integer> update(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody UpdateAppointmentDto appointmentDto) {
        log.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, appointmentDto);
        UpdateAppointmentBo updateAppointmentBo = appointmentMapper.toUpdateAppointmentBo(appointmentDto);
        Integer result = appointmentService.updateAppointment(updateAppointmentBo).getId();
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/{appointmentId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
    public ResponseEntity<AppointmentDto> get(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "appointmentId") Integer appointmentId) {
        log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
        Optional<AppointmentBo> resultService = appointmentService.getAppointment(appointmentId);
        Optional<AppointmentDto> result = resultService.map(appointmentMapper::toAppointmentDto);
        log.debug(OUTPUT, result);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }


	@GetMapping(value="/list/{healthcareProfessionalId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
    public ResponseEntity<Collection<AppointmentListDto>> getList(
            @PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId,
            @RequestParam(name = "diaryIds", defaultValue = "") List<Integer> diaryIds,
			@RequestParam(name = "from", required = false) String from,
			@RequestParam(name = "to", required = false) String to
    ) {
        log.debug("Input parameters -> institutionId {}, diaryIds {}", institutionId, diaryIds);
		LocalDate startDate = (from!=null) ? localDateMapper.fromStringToLocalDate(from) : null;
		LocalDate endDate = (to!=null) ? localDateMapper.fromStringToLocalDate(to) : null;
		Collection<AppointmentBo> resultService = diaryIds.isEmpty() ?
				appointmentService.getAppointmentsByProfessionalInInstitution(healthcareProfessionalId, institutionId, startDate, endDate) :
				appointmentService.getAppointmentsByDiaries(diaryIds, startDate, endDate);
        Set<Integer> patientsIds = resultService.stream().
                filter(appointmentBo -> appointmentBo.getPatientId() != null).
				map(AppointmentBo::getPatientId).collect(Collectors.toSet());
		Set<Integer> bookingAppointmentsIds = resultService.stream().
				filter(appointmentBo -> appointmentBo.getPatientId() == null && !appointmentBo.getAppointmentStateId().equals(AppointmentState.BLOCKED)).
				map(AppointmentBo::getId).collect(Collectors.toSet());

        var bookingPeople = bookingPersonService.getBookingPeople(bookingAppointmentsIds);
        var basicPatientDtoMap = patientExternalService.getBasicDataFromPatientsId(patientsIds);

        Collection<AppointmentListDto> result = resultService.stream()
                .filter(appointmentDto -> appointmentDto.getPatientId() != null)
                .parallel()
                .map(a -> mapData(a, basicPatientDtoMap))
                .collect(Collectors.toList());

		result.addAll(resultService.stream()
				.filter(appointmentDto -> appointmentDto.getAppointmentStateId().equals(AppointmentState.BLOCKED))
				.parallel()
				.map(this::mapToBlockedAppoinments)
				.collect(Collectors.toList()));

		Collection<AppointmentListDto> resultBooking = resultService.stream()
				.filter(appointmentDto -> appointmentDto.getPatientId() == null && !appointmentDto.getAppointmentStateId().equals(AppointmentState.BLOCKED))
				.parallel()
				.map(a -> mapDataBooking(a, bookingPeople))
				.collect(Collectors.toList());
		log.debug("Result size {}", result.size() + resultBooking.size());
		result.addAll(resultBooking);
		log.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

    private AppointmentListDto mapDataBooking(AppointmentBo appointmentBo, Map<Integer, BookingPersonBo> bookingPeople) {
        var bookingPersonBo = bookingPeople.get(appointmentBo.getId());
        return new AppointmentListDto(
                appointmentBo.getId(),
                mapTo(bookingPersonBo),
                appointmentBo.getDate().toString(),
                appointmentBo.getHour().toString(),
                appointmentBo.isOverturn(),
                null,
                null,
                null,
                appointmentBo.getMedicalAttentionTypeId(),
                appointmentBo.getAppointmentStateId(),
                appointmentBo.getPhonePrefix(),
                appointmentBo.getPhoneNumber(),
				appointmentBo.getAppointmentBlockMotiveId(),
				appointmentBo.isProtected()
        );
    }

    private AppointmentBasicPatientDto mapTo(BookingPersonBo bookingPersonBo) {
        if (bookingPersonBo == null)
        	return null;
    	final String PHONE_PREFIX = null;
        final String PHONE_NUMBER = null;
        final String NAME_SELFDETERMINATION = null;

        return new AppointmentBasicPatientDto(
                null,
                new BasicPersonalDataDto(
                        bookingPersonBo.getFirstName(),
                        bookingPersonBo.getLastName(),
                        bookingPersonBo.getIdNumber(),
                        (short) 1,
                        PHONE_PREFIX,
                        PHONE_NUMBER,
                        bookingPersonBo.getGenderId(),
                        NAME_SELFDETERMINATION
                ),
                null);
    }

    private AppointmentListDto mapData(AppointmentBo appointmentBo, Map<Integer, BasicPatientDto> patientData) {
        AppointmentBasicPatientDto appointmentBasicPatientDto = toAppointmentBasicPatientDto(patientData.get(appointmentBo.getPatientId()), appointmentBo.getPhoneNumber(), appointmentBo.getPhonePrefix());
        AppointmentListDto result = appointmentMapper.toAppointmentListDto(appointmentBo, appointmentBasicPatientDto);
        log.debug("AppointmentListDto id result {}", result.getId());
        log.trace(OUTPUT, result);
        return result;
    }

	private AppointmentListDto mapToBlockedAppoinments(AppointmentBo appointmentBo) {
		AppointmentListDto result = appointmentMapper.toAppointmentListDto(appointmentBo, null);
		log.debug("AppointmentListDto id result {}", result.getId());
		log.trace(OUTPUT, result);
		return result;
	}


    @PutMapping(value = "/{appointmentId}/change-state")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Boolean> changeState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidAppointmentDiary @PathVariable(name = "appointmentId") Integer appointmentId,
            @ValidAppointmentState @RequestParam(name = "appointmentStateId") String appointmentStateId,
            @RequestParam(name = "reason", required = false) String reason
    ) {
        log.debug("Input parameters -> institutionId {}, appointmentId {}, appointmentStateId {}", institutionId, appointmentId, appointmentStateId);
        appointmentValidatorService.validateStateUpdate(institutionId, appointmentId, Short.parseShort(appointmentStateId), reason);
        boolean result = appointmentService.updateState(appointmentId, Short.parseShort(appointmentStateId), UserInfo.getCurrentAuditor(), reason);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }


    @GetMapping("/current-appointment")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
    public ResponseEntity<Boolean> hasNewConsultationEnabled(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestParam(name = "patientId") Integer patientId
    ) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        Integer healthProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        boolean result = disableValidation || enableNewConsultation || appointmentService.hasCurrentAppointment(patientId, healthProfessionalId, dateTimeProvider.nowDate());
        log.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/consider-appointment")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Boolean> considerAppointment(
            @PathVariable(name = "institutionId") Integer institutionId
    ) {
        log.debug("Input parameters -> institutionId {}", institutionId);
        boolean result = !disableValidation && !enableNewConsultation;
        log.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @PutMapping(value = "/{appointmentId}/update-phone-number")
    public ResponseEntity<Boolean> updatePhoneNumber(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "appointmentId") Integer appointmentId,
            @RequestParam(required = false) @Size(max = 20, message = "{appointment.new.phoneNumber.invalid}") String phoneNumber,
            @RequestParam(required = false) @Size(max = 10, message = "{appointment.new.phonePrefix.invalid}") String phonePrefix
    ) {
        log.debug("Input parameters -> institutionId {},appointmentId {}, phonePrefix {}, phoneNumber {}", institutionId, appointmentId, phonePrefix, phoneNumber);
        boolean result = appointmentService.updatePhoneNumber(appointmentId, phonePrefix, phoneNumber, UserInfo.getCurrentAuditor());
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	@PutMapping(value = "/{appointmentId}/update-observation")
	public ResponseEntity<Boolean> updateObservation(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestParam(name = "observation") String observation) {
		log.debug("Input parameters -> institutionId {},appointmentId {}, observation {}", institutionId, appointmentId, observation);
		boolean result = appointmentService.saveObservation(appointmentId, observation);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @PutMapping(value = "/{appointmentId}/update-medical-coverage")
    public ResponseEntity<Boolean> updateMedicalCoverage(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestParam(name = "patientMedicalCoverageId", required = false) Integer patientMedicalCoverageId) {
        log.debug("Input parameters -> institutionId {},appointmentId {}, patientMedicalCoverageId {}", institutionId, appointmentId, patientMedicalCoverageId);
        boolean result = appointmentService.updateMedicalCoverage(appointmentId, patientMedicalCoverageId);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	@PutMapping(value = "/{appointmentId}/update-date")
	public ResponseEntity<Boolean> updateDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestBody UpdateAppointmentDateDto updateAppointmentDate) {
		log.debug("Input parameters -> institutionId {},appointmentId {}, fullDate {}, openingHoursId {}", institutionId, appointmentId, updateAppointmentDate.getDate(), updateAppointmentDate.getOpeningHoursId());
		DateTimeDto fullDate = updateAppointmentDate.getDate();
		LocalDate date = dateMapper.fromDateDto(fullDate.getDate());
		LocalTime time = dateMapper.fromTimeDto(fullDate.getTime());
		Integer openingHoursId = updateAppointmentDate.getOpeningHoursId();
		appointmentValidatorService.validateDateUpdate(institutionId, appointmentId, date, time);
		boolean result = appointmentService.updateDate(appointmentId, date, time, openingHoursId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

    @GetMapping("/getDailyAmounts")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
	public ResponseEntity<List<AppointmentDailyAmountDto>> getDailyAmounts(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestParam(name = "diaryId") String diaryId,
			@RequestParam(name = "from") String from,
			@RequestParam(name = "to") String to) {
        log.debug("Input parameters -> diaryId {}", diaryId);

        Integer diaryIdParam = Integer.parseInt(diaryId);
		LocalDate startDate = localDateMapper.fromStringToLocalDate(from);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(to);
        Collection<AppointmentDailyAmountBo> resultService = appointmentDailyAmountService
                .getDailyAmounts(diaryIdParam, startDate, endDate);
        List<AppointmentDailyAmountDto> result = resultService.stream()
                .parallel()
                .map(appointmentMapper::toAppointmentDailyAmountDto)
                .collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{appointmentId}/notifyPatient")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @ResponseStatus(HttpStatus.OK)
    public void notifyPatient(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "appointmentId") Integer appointmentId
    ) {
        notifyPatient.run(institutionId, appointmentId);
    }

    private AppointmentBasicPatientDto toAppointmentBasicPatientDto(BasicPatientDto basicData, String phoneNumber, String phonePrefix) {
        BasicDataPersonDto basicPatientDto = basicData.getPerson();
        BasicPersonalDataDto basicPersonalDataDto = new BasicPersonalDataDto(
                basicPatientDto.getFirstName(),
                basicPatientDto.getLastName(),
                basicPatientDto.getIdentificationNumber(),
                basicPatientDto.getIdentificationTypeId(),
                phonePrefix,
                phoneNumber,
                basicPatientDto.getGender().getId(),
                basicPatientDto.getNameSelfDetermination()
        );
        return new AppointmentBasicPatientDto(basicData.getId(), basicPersonalDataDto, basicData.getTypeId());
    }

    @GetMapping("/{patientId}/get-assigned-appointments")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
    public ResponseEntity<Collection<AssignedAppointmentDto>> getAssignedAppointmentsList(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        var result = appointmentService.getCompleteAssignedAppointmentInfo(patientId).stream().map(appointmentAssigned -> (appointmentMapper.toAssignedAppointmentDto(appointmentAssigned))).collect(Collectors.toList());
        log.debug("Result size {}", result.size());
        log.trace(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

	@GetMapping("/patient/{patientId}/get-medical-coverage")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<ExternalPatientCoverageDto> getCurrentAppointmentMedicalCoverage(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		var pmcBo = appointmentService.getCurrentAppointmentMedicalCoverage(patientId, institutionId);
		if(pmcBo != null){
			var result = ExternalPatientCoverageMapper.mapToExternalPatientCoverageDto(pmcBo);
			log.trace(OUTPUT, result);
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.ok(null);
	}

	@GetMapping("/patient/{patientId}/verify-existing-appointments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<AppointmentShortSummaryDto> getAppointmentFromDeterminatedDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId,
			@RequestParam String date) {
		log.debug("Input parameters -> institutionId {}, patientId {}, date {}", institutionId, patientId, date);
		var appointmentShortSummaryBo = appointmentService.getAppointmentFromDeterminatedDate(patientId, localDateMapper.fromStringToLocalDate(date));
		var result = appointmentMapper.toAppointmentShortSummaryDto(appointmentShortSummaryBo);
		return ResponseEntity.ok(result);
	}

}
