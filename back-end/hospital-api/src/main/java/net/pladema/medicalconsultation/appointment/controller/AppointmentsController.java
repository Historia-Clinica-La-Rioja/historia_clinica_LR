package net.pladema.medicalconsultation.appointment.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.domain.EquipmentBO;
import net.pladema.establishment.service.domain.OrchestratorBO;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentDiary;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentState;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidDetailsOrderImage;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidEquipmentAppointment;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidEquipmentAppointmentDiary;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidTranscribedEquipmentAppointment;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentBasicPatientDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDailyAmountDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentEquipmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentShortSummaryDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AssignedAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.DetailsOrderImageDto;
import net.pladema.medicalconsultation.appointment.controller.dto.EquipmentAppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.StudyIntanceUIDDto;
import net.pladema.medicalconsultation.appointment.controller.dto.UpdateAppointmentDateDto;
import net.pladema.medicalconsultation.appointment.controller.dto.UpdateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.controller.mapper.ExternalPatientCoverageMapper;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentDailyAmountService;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateEquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateTranscribedEquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.EquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.notifypatient.NotifyPatient;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.modality.service.ModalityService;
import net.pladema.modality.service.domain.ModalityBO;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.permissions.repository.enums.ERole;
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

	private final EquipmentAppointmentService equipmentAppointmentService;

	private final AppointmentOrderImageService appointmentOrderImageService;

	private final MoveStudiesService moveStudiesService;

	private final EquipmentService equipmentService;

	private final EquipmentDiaryService equipmentDiaryService;

	private final OrchestratorService orchestratorService;

	private final ModalityService modalityService;

    private final AppointmentValidatorService appointmentValidatorService;

    private final CreateAppointmentService createAppointmentService;

	private final CreateEquipmentAppointmentService createEquipmentAppointmentService;

	private final CreateTranscribedEquipmentAppointmentService createTranscribedEquipmentAppointmentService;

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

	private final MqttClientService mqttClientService;

	public AppointmentsController(
			AppointmentDailyAmountService appointmentDailyAmountService,
			AppointmentService appointmentService, EquipmentAppointmentService equipmentAppointmentService, AppointmentValidatorService appointmentValidatorService,
			CreateAppointmentService createAppointmentService,
			CreateEquipmentAppointmentService createEquipmentAppointmentService,
			CreateTranscribedEquipmentAppointmentService createTranscribedEquipmentAppointmentService,
			AppointmentMapper appointmentMapper,
			PatientExternalService patientExternalService,
			HealthcareProfessionalExternalService healthcareProfessionalExternalService,
			DateTimeProvider dateTimeProvider,
			NotifyPatient notifyPatient,
			BookingPersonService bookingPersonService,
			LocalDateMapper dateMapper,
			LocalDateMapper localDateMapper,
			EquipmentService equipmentService,
			EquipmentDiaryService equipmentDiaryService,
			OrchestratorService orchestratorService,
			MqttClientService mqttClientService,
			ModalityService modalityService,
			AppointmentOrderImageService appointmentOrderImageService,
			MoveStudiesService moveStudiesService) {
        this.appointmentDailyAmountService = appointmentDailyAmountService;
        this.appointmentService = appointmentService;
		this.equipmentAppointmentService = equipmentAppointmentService;
		this.appointmentValidatorService = appointmentValidatorService;
        this.createAppointmentService = createAppointmentService;
		this.createTranscribedEquipmentAppointmentService = createTranscribedEquipmentAppointmentService;
		this.createEquipmentAppointmentService = createEquipmentAppointmentService;
        this.appointmentMapper = appointmentMapper;
        this.patientExternalService = patientExternalService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.dateTimeProvider = dateTimeProvider;
        this.notifyPatient = notifyPatient;
        this.bookingPersonService = bookingPersonService;
		this.dateMapper = dateMapper;
		this.localDateMapper = localDateMapper;
		this.equipmentService = equipmentService;
		this.equipmentDiaryService = equipmentDiaryService;
		this.orchestratorService = orchestratorService;
		this.mqttClientService = mqttClientService;
		this.modalityService = modalityService;
		this.appointmentOrderImageService = appointmentOrderImageService;
		this.moveStudiesService = moveStudiesService;
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

	@PostMapping(value="/equipment")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES')")
	@ValidEquipmentAppointment
	public ResponseEntity<Integer> createEquipmentAppoiment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "orderId", required = false) Integer orderId,
			@RequestParam(name = "studyId", required = false) Integer studyId,
			@RequestBody @Valid CreateAppointmentDto createAppointmentDto
	) {
		log.debug("Input parameters -> institutionId {}, appointmentDto {}, orderId {}, studyId {}", institutionId, createAppointmentDto, orderId, studyId);
		AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
		newAppointmentBo = createEquipmentAppointmentService.execute(newAppointmentBo, orderId, studyId);
		Integer result = newAppointmentBo.getId();
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping(value="/transcribedEquipment")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES')")
	@ValidTranscribedEquipmentAppointment
	public ResponseEntity<Integer> createTranscribedEquipmentAppoiment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "transcribedOrderId", required = false) Integer transcribedOrderId,
			@RequestBody @Valid CreateAppointmentDto createAppointmentDto
	) {
		log.debug("Input parameters -> institutionId {}, appointmentDto {}, transcribedOrderId {}", institutionId, createAppointmentDto, transcribedOrderId);
		AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
		newAppointmentBo = createTranscribedEquipmentAppointmentService.execute(newAppointmentBo, transcribedOrderId);
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
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "appointmentId") Integer appointmentId) {
        log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
        Optional<AppointmentBo> resultService = appointmentService.getAppointment(appointmentId);
        Optional<AppointmentDto> result = resultService.map(appointmentMapper::toAppointmentDto);
        log.debug(OUTPUT, result);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

	@GetMapping(value = "/equipmentAppointment/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<AppointmentDto> getEquipmentAppointment(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "appointmentId") Integer appointmentId) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
		Optional<AppointmentBo> resultService = equipmentAppointmentService.getEquipmentAppointment(appointmentId);
		Optional<AppointmentDto> result = resultService.map(appointmentMapper::toAppointmentDto);
		log.debug(OUTPUT, result);
		return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
	}


	@GetMapping(value="/list/{healthcareProfessionalId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO, ADMINISTRATIVO_RED_DE_IMAGENES')")
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
		Collection<AppointmentListDto> result= dataProcess(resultService);
		log.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value="/list-appoiments-equipment/{equipmentDiaryId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<Collection<AppointmentListDto>> getListAppoitmentsEquipment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "equipmentDiaryId") Integer equipmentDiaryId,
			@RequestParam(name = "from", required = false) String from,
			@RequestParam(name = "to", required = false) String to
	) {
		log.debug("Input parameters -> institutionId {}, equipmentDiaryId {}", institutionId, equipmentDiaryId);
		LocalDate startDate = (from!=null) ? localDateMapper.fromStringToLocalDate(from) : null;
		LocalDate endDate = (to!=null) ? localDateMapper.fromStringToLocalDate(to) : null;
		Collection<AppointmentBo> resultService = appointmentService.getAppointmentsByEquipmentDiary(equipmentDiaryId, startDate, endDate);

		Collection<AppointmentListDto> result= dataProcess(resultService);

		log.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value="/list-appoiments-by-equipment/{equipmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'TECNICO')")
	public ResponseEntity<Collection<EquipmentAppointmentListDto>> getListAppoitmentsEquipment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "equipmentId") Integer equipmentId
	) {
		log.debug("Input parameters -> institutionId {}, equipmentDiaryId {}", institutionId, equipmentId);
		Collection<EquipmentAppointmentBo> resultService = appointmentService.getAppointmentsByEquipmentId(equipmentId, institutionId);

		Collection<EquipmentAppointmentListDto> result= equipmentDataProcess(resultService);

		log.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	private Collection<AppointmentListDto> dataProcess(Collection<AppointmentBo> resultService){
		log.debug("Input parameters -> AppointmentsBo {}", resultService);
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
		return result;
	}

	private Collection<EquipmentAppointmentListDto> equipmentDataProcess(Collection<EquipmentAppointmentBo> resultService){
		log.debug("Input parameters -> AppointmentsBo {}", resultService);
		Set<Integer> patientsIds = resultService.stream().
				filter(equipmentAppointmentBo -> equipmentAppointmentBo.getPatientId() != null).
				map(EquipmentAppointmentBo::getPatientId).collect(Collectors.toSet());
		Set<Integer> bookingAppointmentsIds = resultService.stream().
				filter(appointmentBo -> appointmentBo.getPatientId() == null && !appointmentBo.getAppointmentStateId().equals(AppointmentState.BLOCKED)).
				map(EquipmentAppointmentBo::getId).collect(Collectors.toSet());

		var bookingPeople = bookingPersonService.getBookingPeople(bookingAppointmentsIds);
		var basicPatientDtoMap = patientExternalService.getBasicDataFromPatientsId(patientsIds);

		Collection<EquipmentAppointmentListDto> result = resultService.stream()
				.filter(appointmentDto -> appointmentDto.getPatientId() != null)
				.parallel()
				.map(a -> mapEquipmentData(a, basicPatientDtoMap))
				.collect(Collectors.toList());

		result.addAll(resultService.stream()
				.filter(appointmentDto -> appointmentDto.getAppointmentStateId().equals(AppointmentState.BLOCKED))
				.parallel()
				.map(this::mapToBlockedAppoinments)
				.collect(Collectors.toList()));

		Collection<EquipmentAppointmentListDto> resultBooking = resultService.stream()
				.filter(appointmentDto -> appointmentDto.getPatientId() == null && !appointmentDto.getAppointmentStateId().equals(AppointmentState.BLOCKED))
				.parallel()
				.map(a -> mapEquipmentDataBooking(a, bookingPeople))
				.collect(Collectors.toList());
		log.debug("Result size {}", result.size() + resultBooking.size());
		result.addAll(resultBooking);
		return result;
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

	private EquipmentAppointmentListDto mapEquipmentDataBooking(EquipmentAppointmentBo equipmentAppointmentBo, Map<Integer, BookingPersonBo> bookingPeople) {
		var bookingPersonBo = bookingPeople.get(equipmentAppointmentBo.getId());
		return new EquipmentAppointmentListDto(
				equipmentAppointmentBo.getId(),
				mapTo(bookingPersonBo),
				equipmentAppointmentBo.getDate().toString(),
				equipmentAppointmentBo.getHour().toString(),
				equipmentAppointmentBo.isOverturn(),
				null,
				null,
				null,
				equipmentAppointmentBo.getAppointmentStateId(),
				false
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

	private EquipmentAppointmentListDto mapEquipmentData(EquipmentAppointmentBo equipmentAppointmentBo, Map<Integer, BasicPatientDto> patientData) {
		AppointmentBasicPatientDto appointmentBasicPatientDto = toAppointmentBasicPatientDto(patientData.get(equipmentAppointmentBo.getPatientId()), null, null);
		EquipmentAppointmentListDto result = appointmentMapper.toEquipmentAppointmentListDto(equipmentAppointmentBo, appointmentBasicPatientDto);
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

	private EquipmentAppointmentListDto mapToBlockedAppoinments(EquipmentAppointmentBo equipmentAppointmentBo) {
		EquipmentAppointmentListDto result = appointmentMapper.toEquipmentAppointmentListDto(equipmentAppointmentBo, null);
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

	@PutMapping(value = "/{appointmentId}/equipment-change-state")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, TECNICO')")
	public ResponseEntity<Boolean> equipmentChangeState(
			@PathVariable(name = "institutionId") Integer institutionId,
			@ValidEquipmentAppointmentDiary @PathVariable(name = "appointmentId") Integer appointmentId,
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
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA, PRESCRIPTOR')")
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

	@GetMapping("/publish-work-list/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<Boolean> publishWorkList(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId
	) {
		log.debug("Input parameters -> institutionId {},appointmentId {}", institutionId, appointmentId);
		AppointmentBo appointment = appointmentService.getEquipmentAppointment(appointmentId).orElse(null);
		if (appointment == null){
			return ResponseEntity.ok().body(false);
		}

		Integer diaryId = appointment.getDiaryId();
		CompleteEquipmentDiaryBo equipmentDiary = equipmentDiaryService.getEquipmentDiary(diaryId).orElse(null);
		if (equipmentDiary == null){
			return ResponseEntity.ok().body(false);
		}

		Integer equipmentId = equipmentDiary.getEquipmentId();
		EquipmentBO equipmentBO =equipmentService.getEquipment(equipmentId);
		if (equipmentBO == null){
			return ResponseEntity.ok().body(false);
		}

		Integer orchestratorId = equipmentBO.getOrchestratorId();
		OrchestratorBO orchestrator = orchestratorService.getOrchestrator(orchestratorId);
		if (orchestrator == null){
			return ResponseEntity.ok().body(false);
		}

		ModalityBO modalityBO = modalityService.getModality(equipmentBO.getModalityId());
		if (modalityBO == null){
			return ResponseEntity.ok().body(false);
		}

		Integer patientId =appointment.getPatientId();
		BasicPatientDto basicDataPatient = patientExternalService.getBasicDataFromPatient(patientId);
		if (basicDataPatient == null){
			return ResponseEntity.ok().body(false);
		}


		String UID= "1." + ThreadLocalRandom.current().nextInt(1,10) + "." +
					ThreadLocalRandom.current().nextInt(1,10) + "." +
					ThreadLocalRandom.current().nextInt(1,100) + "." +
					ThreadLocalRandom.current().nextInt(1,100) + "." +
					ThreadLocalRandom.current().nextInt(1,1000) + "." +
					ThreadLocalRandom.current().nextInt(1,1000) + "." +
					basicDataPatient.getIdentificationNumber();
		String date = appointment.getDate().toString().replace("-","");
		String time = appointment.getHour().toString().replace(":","") + "00";
		String birthDate = null;
		char gender = 0;
		BasicDataPersonDto person = basicDataPatient.getPerson();
		if (person != null) {
			birthDate = person.getBirthDate() != null ? person.getBirthDate().toString().replace("-", "") : null;

			gender = person.getGender() != null 
					&& person.getGender().getDescription() != null 
					&& basicDataPatient.getPerson().getGender().getDescription().toCharArray().length > 0 ? basicDataPatient.getPerson().getGender().getDescription().toCharArray()[0] : ' ';
		}
		String accessionNumber =   "   \"AccessionNumber\": \"" + institutionId +"-" + appointmentId + "\", \n";
		String studyInstanceUID =   "   \"StudyInstanceUID\": \"" + UID+ "\", \n";
		String aeTitle =   "    \"ScheduledStationAETitle\": \"" + equipmentBO.getAeTitle() + "\",\n";
		String startDate = "    \"ScheduledProcedureStepStartDate\": \"" + date + "\",\n";
		String startTime = "    \"ScheduledProcedureStepStartTime\": \"" + time + "\",\n";
		String patientIdStr = "    \"PatientID\": \"" + basicDataPatient.getIdentificationNumber() + "\",\n";
		String patientName = "    \"PatientName\": \"" + basicDataPatient.getFirstName() + " " + basicDataPatient.getLastName() + "\",\n";
		String patientBirthDate = "    \"PatientBirthDate\": \"" + birthDate + "\",\n";
		String patientSex = "    \"PatientSex\": \"" + gender + "\",\n";
		String studyDescription = "    \"StudyDescription\": \"" + "description order" + "\",\n";
		String modality = "    \"Modality\": \"" + modalityBO.getAcronym() + "\"\n";
		String json =  "{\n"
				+ accessionNumber
				+ studyInstanceUID
				+ aeTitle
				+ startDate
				+ startTime
				+ patientIdStr
				+ patientName
				+ patientSex
				+ studyDescription
				+ patientBirthDate
				+ modality
				+ "}";

		String topic = orchestrator.getBaseTopic() + "/LISTATRABAJO";


		MqttMetadataBo data = new MqttMetadataBo(topic, json,false,2);
		mqttClientService.publish(data);
		appointmentOrderImageService.setImageId(appointmentId,	UID);
		return ResponseEntity.ok().body(true);
	}

	@GetMapping("/get-study-instance-UID/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, INFORMADOR')")
	public ResponseEntity<StudyIntanceUIDDto> getStudyInstanceUID(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId
	) {
		String imageId = appointmentOrderImageService.getImageId(appointmentId).orElse("none");
		StudyIntanceUIDDto uid = new StudyIntanceUIDDto(imageId);
		return ResponseEntity.ok().body(uid);
	}

	@PostMapping("/study-observations/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'TECNICO')")
	@ValidDetailsOrderImage
	public ResponseEntity<Boolean> addStudyObservations(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestBody DetailsOrderImageDto detailsOrderImageDto
			) {
		Integer technicianId = UserInfo.getCurrentAuditor();
		log.debug("Input parameters -> institutionId {}, appointmentId {}, technicianId {}, {}", institutionId, appointmentId, technicianId, detailsOrderImageDto);
		Short roleId = ERole.TECNICO.getId();
		DetailsOrderImageBo detailsOrderImageBo = new DetailsOrderImageBo(appointmentId, detailsOrderImageDto.getObservations(), LocalDateTime.now(), technicianId, roleId);
		boolean result = appointmentOrderImageService.updateCompleted(detailsOrderImageBo, true);
		Integer idMove = moveStudiesService.create(appointmentId);
		moveStudiesService.getSizeFromOrchestrator(idMove);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRATIVO_RED_DE_IMAGENES')")
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

    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRATIVO_RED_DE_IMAGENES')")
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
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA, PRESCRIPTOR')")
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
			@RequestParam String date,
			@RequestParam String hour) {
		log.debug("Input parameters -> institutionId {}, patientId {}, date {}", institutionId, patientId, date);
		var appointmentShortSummaryBo = appointmentService.getAppointmentFromDeterminatedDate(patientId, institutionId, localDateMapper.fromStringToLocalDate(date), localDateMapper.fromStringToLocalTime(hour));
		var result = appointmentMapper.toAppointmentShortSummaryDto(appointmentShortSummaryBo);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/patient/{patientId}/verify-existing-appointments-equipment")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<AppointmentEquipmentShortSummaryDto> getAppointmentEquipmentFromDeterminatedDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId,
			@RequestParam String date) {
		log.debug("Input parameters -> institutionId {}, patientId {}, date {}", institutionId, patientId, date);
		var appointmentShortSummaryBo = appointmentService.getAppointmentEquipmentFromDeterminatedDate(patientId, localDateMapper.fromStringToLocalDate(date));
		var result = appointmentMapper.toAppointmentEquipmentShortSummaryDto(appointmentShortSummaryBo);
		return ResponseEntity.ok(result);
	}

}
