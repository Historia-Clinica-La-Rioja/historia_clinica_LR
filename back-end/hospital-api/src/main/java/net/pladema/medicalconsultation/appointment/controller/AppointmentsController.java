package net.pladema.medicalconsultation.appointment.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import net.pladema.medicalconsultation.appointment.application.ChangeAppointmentState;
import net.pladema.medicalconsultation.appointment.application.GetAppointment;
import net.pladema.medicalconsultation.appointment.application.createexpiredappointment.CreateExpiredAppointment;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentOrderDetailImageDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.DetailOrderImageMapper;
import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentStateBo;
import net.pladema.medicalconsultation.appointment.infrastructure.input.rest.dto.UpdateAppointmentStateDto;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentLabel;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBookingBo;
import net.pladema.medicalconsultation.appointment.service.exceptions.AlreadyPublishedWorklistException;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryLabelDto;
import net.pladema.medicalconsultation.appointment.application.ReassignAppointment;

import net.pladema.medicalconsultation.appointment.domain.UpdateAppointmentDateBo;

import net.pladema.staff.controller.dto.ProfessionalDto;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.medicalconsultation.appointment.controller.dto.CustomRecurringAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateCustomAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.WeekDayDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.WeekDayMapper;
import net.pladema.medicalconsultation.appointment.infrastructure.output.repository.appointment.RecurringAppointmentType;
import net.pladema.medicalconsultation.appointment.service.CancelRecurringAppointment;
import net.pladema.medicalconsultation.appointment.service.CreateCustomAppointmentService;
import net.pladema.medicalconsultation.appointment.service.CreateEveryWeekAppointmentService;
import net.pladema.medicalconsultation.appointment.service.FetchCustomAppointment;
import net.pladema.medicalconsultation.appointment.service.FetchWeekDay;

import net.pladema.medicalconsultation.appointment.service.domain.CreateCustomAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.RecurringTypeBo;

import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;

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
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.establishment.controller.dto.HierarchicalUnitDto;
import net.pladema.establishment.controller.mapper.InstitutionMapper;
import net.pladema.establishment.service.domain.HierarchicalUnitBo;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.medicalconsultation.appointment.application.GetCurrentAppointmentHierarchicalUnit.GetCurrentAppointmentHierarchicalUnit;
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
import net.pladema.medicalconsultation.appointment.controller.dto.BookedAppointmentDto;
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
import net.pladema.medicalconsultation.appointment.service.DeriveReportService;
import net.pladema.medicalconsultation.appointment.service.EquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.booking.BookingPersonService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.appointment.service.domain.DetailsOrderImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.exceptions.NotifyPatientException;
import net.pladema.medicalconsultation.appointment.service.notifypatient.NotifyPatient;
import net.pladema.patient.controller.mapper.PatientMedicalCoverageMapper;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments")
@Tag(name = "Appointments", description = "Appointments")
@Validated
@RequiredArgsConstructor
public class AppointmentsController {

	public static final String OUTPUT = "Output -> {}";

	private final Long MAX_DAYS = 90L;

	private final AppointmentDailyAmountService appointmentDailyAmountService;

	private final AppointmentService appointmentService;

	private final EquipmentAppointmentService equipmentAppointmentService;

	private final AppointmentOrderImageService appointmentOrderImageService;

	private final MoveStudiesService moveStudiesService;

	private final AppointmentValidatorService appointmentValidatorService;

	private final CreateAppointmentService createAppointmentService;

	private final CreateEquipmentAppointmentService createEquipmentAppointmentService;

	private final CreateTranscribedEquipmentAppointmentService createTranscribedEquipmentAppointmentService;

	private final AppointmentMapper appointmentMapper;

	private final InstitutionMapper institutionMapper;

	private final PatientExternalService patientExternalService;

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final DateTimeProvider dateTimeProvider;

	private final NotifyPatient notifyPatient;

	private final BookingPersonService bookingPersonService;

	private final DeriveReportService deriveReportService;

	private final LocalDateMapper localDateMapper;

	private final PatientMedicalCoverageMapper patientMedicalCoverageMapper;

	private final DetailOrderImageMapper detailOrderImageMapper;

	private final MqttClientService mqttClientService;

	private final PatientMedicalCoverageService patientMedicalCoverageService;

	private final FeatureFlagsService featureFlagsService;

	private final GetCurrentAppointmentHierarchicalUnit getCurrentAppointmentHierarchicalUnit;

	private final CreateAppointmentLabel createAppointmentLabel;
	
	private final ReassignAppointment reassignAppointment;

	private final SharedBookingPort sharedBookingPort;

	private final FetchWeekDay fetchWeekDay;

	private final WeekDayMapper weekDayMapper;

	private final CreateEveryWeekAppointmentService createEveryWeekAppointmentService;

	private final CreateCustomAppointmentService createCustomAppointmentService;

	private final CancelRecurringAppointment cancelRecurringAppointment;

	private final FetchCustomAppointment fetchCustomAppointment;

	private final CreateExpiredAppointment createExpiredAppointment;

	private final ChangeAppointmentState changeAppointmentState;

	private final GetAppointment getAppointment;

	@Value("${test.stress.disable.validation:false}")
	private boolean disableValidation;

	@Value("${habilitar.boton.consulta:false}")
	private boolean enableNewConsultation;

	@Value("${scheduledjobs.updateappointmentsstate.pastdays:1}")
	private Long PAST_DAYS;

	@PostMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO') || hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	@ValidAppointment
	public ResponseEntity<Integer> create(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody @Valid CreateAppointmentDto createAppointmentDto
	) {
		log.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
		AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
		newAppointmentBo.setRecurringTypeBo(new RecurringTypeBo(RecurringAppointmentType.NO_REPEAT.getId(), RecurringAppointmentType.NO_REPEAT.getValue()));
		newAppointmentBo = createAppointmentService.execute(newAppointmentBo);
		Integer result = newAppointmentBo.getId();
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/expired")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	@ValidAppointment
	public ResponseEntity<Integer> createExpiredAppointment(@PathVariable(name = "institutionId") Integer institutionId,
															@RequestBody @Valid CreateAppointmentDto createAppointmentDto) {
		log.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
		AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
		newAppointmentBo.setRecurringTypeBo(new RecurringTypeBo(RecurringAppointmentType.NO_REPEAT.getId(), RecurringAppointmentType.NO_REPEAT.getValue()));
		Integer result = createExpiredAppointment.run(newAppointmentBo, institutionId);
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
		newAppointmentBo = createEquipmentAppointmentService.execute(newAppointmentBo, orderId, studyId, institutionId);
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
		newAppointmentBo = createTranscribedEquipmentAppointmentService.execute(newAppointmentBo, transcribedOrderId, institutionId);
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
		Optional<AppointmentBo> resultService = getAppointment.run(appointmentId);
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
		if (result.isPresent() && result.get().getOrderData() != null && result.get().getOrderData().getServiceRequestId() != null){
			PatientMedicalCoverageBo coverage = patientMedicalCoverageService.getActiveCoveragesByOrderId(result.get().getOrderData().getServiceRequestId());
			result.get().getOrderData().setCoverageDto(patientMedicalCoverageMapper.toPatientMedicalCoverageDto(coverage));
		}
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
	public ResponseEntity<Collection<EquipmentAppointmentListDto>> getListAppoitmentsByEquipment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "equipmentId") Integer equipmentId,
			@RequestParam(name = "from", required = false) String from,
			@RequestParam(name = "to", required = false) String to
	) {
		log.debug("Input parameters -> institutionId {}, equipmentDiaryId {}", institutionId, equipmentId);
		LocalDate startDate = (from!=null) ? localDateMapper.fromStringToLocalDate(from) : null;
		LocalDate endDate = (to!=null) ? localDateMapper.fromStringToLocalDate(to) : null;
		Collection<EquipmentAppointmentBo> resultService = appointmentService.getAppointmentsByEquipmentId(equipmentId, institutionId, startDate, endDate);

		Collection<EquipmentAppointmentListDto> result = equipmentDataProcess(resultService);

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
		Set<Integer> patientsIds = resultService.stream()
				.map(EquipmentAppointmentBo::getPatientId)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		Set<Integer> bookingAppointmentsIds = resultService.stream()
				.filter(appointmentBo -> appointmentBo.getPatientId() == null && !appointmentBo.getAppointmentStateId().equals(AppointmentState.BLOCKED))
				.map(EquipmentAppointmentBo::getId)
				.collect(Collectors.toSet());

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
				appointmentBo.isProtected(),
				appointmentBo.getCreatedOn(),
				localDateMapper.toDateTimeDto(appointmentBo.getUpdatedOn()),
				appointmentBo.getProfessionalPersonBo() != null
						? new ProfessionalPersonDto(
						appointmentBo.getProfessionalPersonBo().getId(),
						appointmentBo.getProfessionalPersonBo().getFullName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)))
						:  null,
				appointmentBo.getDiaryLabelBo() != null ? new DiaryLabelDto(appointmentBo.getDiaryLabelBo()): null,
				appointmentBo.getPatientEmail(),
				appointmentBo.isExpiredRegister()
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
				false,
				institutionMapper.fromInstitutionBasicInfoBo(equipmentAppointmentBo.getDerivedTo()),
				equipmentAppointmentBo.getReportStatusId(),
				equipmentAppointmentBo.getStudies().get(0),
				equipmentAppointmentBo.getStudies(),
				equipmentAppointmentBo.getServiceRequestId(),
				equipmentAppointmentBo.getTranscribedServiceRequestId(),
				null,
				equipmentAppointmentBo.getLocalViewerUrl()
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
		var professionalPersonBo = appointmentBo.getProfessionalPersonBo();
		if (professionalPersonBo != null)
			result.getProfessionalPersonDto().setFullName(professionalPersonBo.getFullName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));
		log.debug("AppointmentListDto id result {}", result.getId());
		log.trace(OUTPUT, result);
		return result;
	}

	private EquipmentAppointmentListDto mapEquipmentData(EquipmentAppointmentBo equipmentAppointmentBo, Map<Integer, BasicPatientDto> patientData) {
		AppointmentBasicPatientDto appointmentBasicPatientDto = toAppointmentBasicPatientDto(patientData.get(equipmentAppointmentBo.getPatientId()), null, null);
		EquipmentAppointmentListDto result = appointmentMapper.toEquipmentAppointmentListDto(equipmentAppointmentBo, appointmentBasicPatientDto);
		result.mapTranscribedOrderAttachedFiles(equipmentAppointmentBo.getTranscribedOrderAttachedFiles());
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
	public ResponseEntity<Boolean> changeState(@PathVariable(name = "institutionId") Integer institutionId,
											   @ValidAppointmentDiary @PathVariable(name = "appointmentId") Integer appointmentId,
											   @ValidAppointmentState @RequestParam(name = "appointmentStateId") String appointmentStateId,
											   @RequestBody UpdateAppointmentStateDto updateAppointmentStateDto) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}, appointmentStateId {}, updateAppointmentStateDto {}", institutionId, appointmentId, appointmentStateId, updateAppointmentStateDto);
		appointmentValidatorService.validateStateUpdate(institutionId, appointmentId, Short.parseShort(appointmentStateId), updateAppointmentStateDto.getReason());
		UpdateAppointmentStateBo updateAppointmentStateBo = appointmentMapper.fromUpdateAppointmentStateDto(updateAppointmentStateDto, appointmentId, Short.parseShort(appointmentStateId));
		boolean result = changeAppointmentState.run(updateAppointmentStateBo);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping(value = "/{appointmentId}/derive-report")
	@PreAuthorize("hasPermission(#institutionId, 'TECNICO, INFORMADOR')")
	public ResponseEntity<Boolean> deriveReport(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestParam(name = "destInstitutionId") Integer destInstitutionId) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}, destInstitutionId {}", institutionId, appointmentId, destInstitutionId);
		boolean result = deriveReportService.execute(destInstitutionId, appointmentId);
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

		var stateId = Short.parseShort(appointmentStateId);
		Supplier<Boolean> updateState = () -> appointmentService.updateState(appointmentId, stateId, UserInfo.getCurrentAuditor(), reason);

		appointmentValidatorService.validateStateUpdate(institutionId, appointmentId, stateId, reason);

		if (stateId != 2) {
			return ResponseEntity.ok().body(updateState.get());
		}

		try {
			MqttMetadataBo data = equipmentAppointmentService.publishWorkList(institutionId, appointmentId);
			if (data != null){
				mqttClientService.publish(data);
				return ResponseEntity.ok().body(updateState.get());
			}
			log.warn("Not publishWorkList -> institutionId {},appointmentId {}", institutionId, appointmentId);
			return ResponseEntity.ok().body(false);
		} catch (AlreadyPublishedWorklistException e) {
			return ResponseEntity.ok().body(updateState.get());
		}

	}


	@GetMapping("/current-appointment")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA, PRESCRIPTOR, ABORDAJE_VIOLENCIAS')")
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
		return ResponseEntity.ok().body(true);
	}

	@GetMapping("/get-study-instance-UID/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO_RED_DE_IMAGENES, INFORMADOR, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
	public ResponseEntity<StudyIntanceUIDDto> getStudyInstanceUID(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId
	) {
		String imageId = appointmentOrderImageService.getImageId(appointmentId).orElse("none");
		StudyIntanceUIDDto uid = new StudyIntanceUIDDto(imageId);
		return ResponseEntity.ok().body(uid);
	}

	@PostMapping("/{appointmentId}/require-report")
	@PreAuthorize("hasPermission(#institutionId, 'TECNICO, INFORMADOR')")
	public ResponseEntity<Boolean> updateReportStatus(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
		appointmentOrderImageService.setReportStatusId(appointmentId, EDiagnosticImageReportStatus.PENDING.getId());
		log.debug(OUTPUT, Boolean.TRUE);
		return ResponseEntity.ok().body(Boolean.TRUE);
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
		DetailsOrderImageBo detailsOrderImageBo = new DetailsOrderImageBo(appointmentId, detailsOrderImageDto.getObservations(), LocalDateTime.now(), technicianId, detailsOrderImageDto.getIsReportRequired());
		appointmentOrderImageService.updateCompleted(detailsOrderImageBo);
		Integer idMove = moveStudiesService.create(appointmentId, institutionId);
		moveStudiesService.getSizeFromOrchestrator(idMove);
		log.debug(OUTPUT, Boolean.TRUE);
		return ResponseEntity.ok().body(Boolean.TRUE);
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

	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	@PutMapping(value = "/{appointmentId}/update-orderId")
	public ResponseEntity<Boolean> updateOrderId(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestParam(name = "orderId", required = false) Integer orderId,
			@RequestParam(name = "studyId", required = false) Integer studyId,
			@RequestParam(name = "transcribed") boolean isTranscribed){
		log.debug("Input parameters -> institutionId {}, appointmentId {}, orderId {}", institutionId, appointmentId, orderId);
		boolean result = appointmentOrderImageService.updateOrderId(appointmentId, orderId, isTranscribed, studyId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	@PutMapping(value = "/{appointmentId}/update-date")
	public ResponseEntity<Boolean> updateDate(@PathVariable(name = "institutionId") Integer institutionId,
											  @PathVariable(name = "appointmentId") Integer appointmentId,
											  @RequestBody UpdateAppointmentDateDto updateAppointmentDate) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}, updateAppointmentDate {}", institutionId, appointmentId, updateAppointmentDate);
		UpdateAppointmentDateBo updateAppointmentData = appointmentMapper.fromUpdateAppointmentDateDto(updateAppointmentDate);
		appointmentValidatorService.validateDateUpdate(institutionId, appointmentId, updateAppointmentData.getDate(), updateAppointmentData.getTime());
		boolean result = reassignAppointment.run(updateAppointmentData);
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
	) throws NotifyPatientException {
		notifyPatient.run(institutionId, appointmentId);
	}

	private AppointmentBasicPatientDto toAppointmentBasicPatientDto(BasicPatientDto basicData, String phoneNumber, String phonePrefix) {
		BasicDataPersonDto basicPatientDto = basicData.getPerson();
		BasicPersonalDataDto basicPersonalDataDto = new BasicPersonalDataDto(
				basicPatientDto.getFirstName(),
				basicPatientDto.getMiddleNames(),
				basicPatientDto.getLastName(),
				basicPatientDto.getOtherLastNames(),
				basicPatientDto.getIdentificationNumber(),
				basicPatientDto.getIdentificationTypeId(),
				phonePrefix,
				phoneNumber,
				basicPatientDto.getGender().getId(),
				basicPatientDto.getNameSelfDetermination(),
				null,
				basicPatientDto.getBirthDate()
		);
		return new AppointmentBasicPatientDto(basicData.getId(), basicPersonalDataDto, basicData.getTypeId());
	}

	@GetMapping("/{patientId}/get-assigned-appointments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Collection<AssignedAppointmentDto>> getAssignedAppointmentsList(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		LocalDate minDate = LocalDate.now().minusDays(PAST_DAYS);
		LocalDate maxDate = LocalDate.now().plusDays(MAX_DAYS);
		var result = appointmentService.getCompleteAssignedAppointmentInfo(patientId, minDate, maxDate).stream().map(appointmentMapper::toAssignedAppointmentDto).collect(Collectors.toList());
		log.debug("Result size {}", result.size());
		log.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{identificationNumber}/get-booking-appointments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public List<BookedAppointmentDto> getBookingAppointmentsList(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "identificationNumber") String identificationNumber) {
		log.debug("Input parameters -> institutionId {}, identificationNumber {}", institutionId, identificationNumber);
		List<AppointmentBookingBo> bookedAppointments = appointmentService.getCompleteBookingAppointmentInfo(identificationNumber);
		List<BookedAppointmentDto> result = appointmentMapper.toBookingAppointmentDtoList(bookedAppointments);
		log.debug(OUTPUT, result);
		return result;
	}

	@GetMapping("/patient/{patientId}/get-medical-coverage")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA, PRESCRIPTOR, ABORDAJE_VIOLENCIAS')")
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
	@PreAuthorize("hasAnyAuthority('ADMINISTRATIVO', 'ESPECIALISTA_MEDICO', 'PROFESIONAL_DE_SALUD', 'ESPECIALISTA_EN_ODONTOLOGIA', 'ENFERMERO', 'GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
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
		
	@PostMapping(value = "/every-week-save")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Boolean> everyWeekSave(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody CreateAppointmentDto createAppointmentDto) {
		log.debug("Input parameters -> institutionId {}, createAppointmentDto {}", institutionId, createAppointmentDto);
		LocalDate diaryEndDate = appointmentValidatorService.checkAppointmentEveryWeek(
				createAppointmentDto.getHour(),
				createAppointmentDto.getDate(),
				createAppointmentDto.getDiaryId(),
				createAppointmentDto.getId(),
				createAppointmentDto.getAppointmentOptionId(),
				createAppointmentDto.getOpeningHoursId());
		boolean result = createEveryWeekAppointmentService.execute(appointmentMapper.toAppointmentBo(createAppointmentDto), diaryEndDate);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/patient/{patientId}/has-current-appointment")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Integer> hasCurrentAppointment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		Integer result = appointmentService.patientHasCurrentAppointment(institutionId, patientId);
		log.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/patient/{patientId}/get-hierarchical-unit")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<HierarchicalUnitDto> getCurrentAppointmentHierarchicalUnit(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "patientId") Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		HierarchicalUnitBo hierarchicalUnitBo= getCurrentAppointmentHierarchicalUnit.run(institutionId, patientId);
		if (hierarchicalUnitBo != null) {
			var result = new HierarchicalUnitDto(hierarchicalUnitBo.getId(), hierarchicalUnitBo.getName());
			log.trace(OUTPUT, result);
			return ResponseEntity.ok(result);
		}
		return null;
	}

	@PostMapping(value = "/{appointmentId}/label")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Boolean> updateLabel(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestBody(required = false) Integer diaryLabelId) {
		log.debug("Input parameters -> institutionId {}, diaryLabelId {}, appointmentId {}", institutionId, diaryLabelId, appointmentId);
		Boolean result = createAppointmentLabel.execute(diaryLabelId, appointmentId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping(value = "/custom-save")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Boolean> customSave(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody CreateCustomAppointmentDto createCustomAppointmentDto) {
		log.debug("Input parameters -> institutionId {}, createCustomAppointmentDto {}", institutionId, createCustomAppointmentDto);

		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO))
			return ResponseEntity.ok().body(false);

		CreateCustomAppointmentBo bo = toCreateCustomAppointmentBo(createCustomAppointmentDto);
		appointmentValidatorService.checkCustomAppointment(bo);
		boolean result = createCustomAppointmentService.execute(bo);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("{appointmentId}/detailOrderImage/transcribed-order/{transcribed}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA, ADMINISTRATIVO_RED_DE_IMAGENES, TECNICO')")
	public ResponseEntity<AppointmentOrderDetailImageDto> getOrderDetailImage(@PathVariable(name = "institutionId") Integer institutionId,
																			  @PathVariable(name = "appointmentId") Integer appointmentId,
																			  @PathVariable(name = "transcribed") Boolean isTranscribed) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}, isTranscribed {}", institutionId, appointmentId, isTranscribed);
		AppointmentOrderDetailImageDto result;
		var bo = this.appointmentOrderImageService.getDetailOrdenImageTechnical(appointmentId, isTranscribed);
		if (!isTranscribed) {
			ProfessionalDto professionalDto = bo.getIdDoctor() == null ? null :
					healthcareProfessionalExternalService.findProfessionalByUserId(bo.getIdDoctor());
			result = this.detailOrderImageMapper.parseToAppointmentOrderDetailDto(bo, professionalDto);
		} else {
			result = this.detailOrderImageMapper.parseToAppointmentOrderTranscribedDetailDto(bo);
		}
		log.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/third-party")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	@ResponseStatus(HttpStatus.CREATED)
	public SavedBookingAppointmentDto createThirdPartyAppointment(@PathVariable("institutionId") Integer institutionId,
													  @RequestBody BookingDto bookingDto) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException, SaveExternalBookingException {
		log.debug("Input parameters -> institutionId {}, bookingDto {}", institutionId, bookingDto);
		SavedBookingAppointmentDto result = sharedBookingPort.makeBooking(bookingDto, false);
		log.debug("Output -> {}", result);
		return result;
	}

	@PutMapping(value = "/{appointmentId}/no-repeat")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Boolean> cancelRecurringAppointments(@PathVariable(name = "institutionId") Integer institutionId,
															   @PathVariable(name = "appointmentId") Integer appointmentId) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);

		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO))
			return ResponseEntity.ok().body(false);

		Boolean result = cancelRecurringAppointment.execute(appointmentId);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping(value = "/{appointmentId}/cancel-recurring-appointments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Boolean> cancelRecurringAppointments(@PathVariable(name = "institutionId") Integer institutionId,
															   @PathVariable(name = "appointmentId") Integer appointmentId,
															   @RequestParam(name = "cancelAllAppointments") Boolean cancelAllAppointments) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}, cancelAllAppointments {}", institutionId, appointmentId, cancelAllAppointments);

		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO))
			return ResponseEntity.ok().body(false);

		Boolean result = cancelRecurringAppointment.execute(appointmentId, cancelAllAppointments);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/{appointmentId}/custom-appointment")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<CustomRecurringAppointmentDto> getCustomAppointment(@PathVariable(name = "institutionId") Integer institutionId,
																			  @PathVariable(name = "appointmentId") Integer appointmentId) {
		log.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);

		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO))
			return ResponseEntity.ok().body(new CustomRecurringAppointmentDto());

		CustomRecurringAppointmentBo bo = fetchCustomAppointment.execute(appointmentId);
		CustomRecurringAppointmentDto result = appointmentMapper.toCustomRecurringAppointmentDto(bo);
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/week-day")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<WeekDayDto>> getWeekDay(@PathVariable(name = "institutionId") Integer institutionId) {
		List<WeekDayDto> result = fetchWeekDay.run()
				.stream()
				.map(weekDayMapper::toWeekDayDto)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

	private CreateCustomAppointmentBo toCreateCustomAppointmentBo(CreateCustomAppointmentDto dto) {
		CreateCustomAppointmentBo createCustomAppointmentBo = new CreateCustomAppointmentBo(
				new AppointmentBo(
						dto.getCreateAppointmentDto().getDiaryId(),
						dto.getCreateAppointmentDto().getPatientId(),
						localDateMapper.fromStringToLocalDate(dto.getCreateAppointmentDto().getDate()),
						localDateMapper.fromStringToLocalTime(dto.getCreateAppointmentDto().getHour()),
						dto.getCreateAppointmentDto().getOpeningHoursId(),
						dto.getCreateAppointmentDto().isOverturn(),
						dto.getCreateAppointmentDto().getPatientMedicalCoverageId(),
						dto.getCreateAppointmentDto().getPhonePrefix(),
						dto.getCreateAppointmentDto().getPhoneNumber(),
						dto.getCreateAppointmentDto().getModality().getId()),
				new CustomRecurringAppointmentBo(
						dto.getCustomRecurringAppointmentDto().getEndDate(),
						dto.getCustomRecurringAppointmentDto().getRepeatEvery(),
						dto.getCustomRecurringAppointmentDto().getWeekDayId()
				)
		);
		createCustomAppointmentBo.getCreateAppointmentBo().setId(dto.getCreateAppointmentDto().getId());
		createCustomAppointmentBo.getCreateAppointmentBo().setAppointmentOptionId(dto.getCreateAppointmentDto().getAppointmentOptionId());
		return  createCustomAppointmentBo;
	}
}
