package net.pladema.medicalconsultation.appointment.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import net.pladema.medicalconsultation.appointment.controller.dto.AssignedAppointmentDto;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentDiary;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentState;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentBasicPatientDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDailyAmountDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.service.AppointmentDailyAmountService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDailyAmountBo;
import net.pladema.medicalconsultation.appointment.service.notifypatient.NotifyPatient;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments")
@Tag(name = "Appointments", description = "Appointments")
@Validated
public class AppointmentsController {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentsController.class);

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

    @Value("${test.stress.disable.validation:false}")
    private boolean disableValidation;

    @Value("${habilitar.boton.consulta:false}")
    private boolean enableNewConsultation;

    public AppointmentsController(AppointmentDailyAmountService appointmentDailyAmountService,
    AppointmentService appointmentService,
    AppointmentValidatorService appointmentValidatorService,
    CreateAppointmentService createAppointmentService,
    AppointmentMapper appointmentMapper,
    PatientExternalService patientExternalService,
    HealthcareProfessionalExternalService healthcareProfessionalExternalService,
    DateTimeProvider dateTimeProvider,
    NotifyPatient notifyPatient) {
        super();
        this.appointmentDailyAmountService = appointmentDailyAmountService;
        this.appointmentService = appointmentService;
        this.appointmentValidatorService = appointmentValidatorService;
        this.createAppointmentService = createAppointmentService;
        this.appointmentMapper = appointmentMapper;
        this.patientExternalService = patientExternalService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.dateTimeProvider = dateTimeProvider;
        this.notifyPatient = notifyPatient;
	}

    @Transactional
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @ValidAppointment
    public ResponseEntity<Integer> create(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody @Valid CreateAppointmentDto createAppointmentDto) {
        LOG.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
        AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
        newAppointmentBo = createAppointmentService.execute(newAppointmentBo);
        Integer result = newAppointmentBo.getId();
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping(value = "/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
	public ResponseEntity<AppointmentDto> get(@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId) {
		LOG.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
		Optional<AppointmentBo> resultService = appointmentService.getAppointment(appointmentId);
		Optional<AppointmentDto> result = resultService.map(appointmentMapper::toAppointmentDto);
		LOG.debug(OUTPUT, result);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }


    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
    public ResponseEntity<Collection<AppointmentListDto>> getList(@PathVariable(name = "institutionId")  Integer institutionId,
                                                                  @RequestParam(name = "diaryIds") @NotEmpty List<Integer> diaryIds){
        LOG.debug("Input parameters -> institutionId {}, diaryIds {}", institutionId, diaryIds);
        Collection<AppointmentBo> resultService = appointmentService.getAppointmentsByDiaries(diaryIds);
        Set<Integer> patientsIds = resultService.stream().map(AppointmentBo::getPatientId).collect(Collectors.toSet());

        var basicPatientDtoMap = patientExternalService.getBasicDataFromPatientsId(patientsIds);
        Collection<AppointmentListDto> result = resultService.stream()
                .parallel()
                .map(a -> mapData(a, basicPatientDtoMap))
                .collect(Collectors.toList());
        LOG.debug("Result size {}", result.size());
        LOG.trace(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    private AppointmentListDto mapData(AppointmentBo appointmentBo, Map<Integer, BasicPatientDto> patientData) {
		AppointmentBasicPatientDto appointmentBasicPatientDto = toAppointmentBasicPatientDto(patientData.get(appointmentBo.getPatientId()),appointmentBo.getPhoneNumber(), appointmentBo.getPhonePrefix());
		AppointmentListDto result = appointmentMapper.toAppointmentListDto(appointmentBo, appointmentBasicPatientDto);
        LOG.debug("AppointmentListDto id result {}", result.getId());
        LOG.trace(OUTPUT, result);
        return result;
    }


    @Transactional
    @PutMapping(value = "/{appointmentId}/change-state")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Boolean> changeState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidAppointmentDiary @PathVariable(name = "appointmentId") Integer appointmentId,
            @ValidAppointmentState @RequestParam(name = "appointmentStateId") String appointmentStateId,
            @RequestParam(name = "reason", required = false) String reason) {
		LOG.debug("Input parameters -> institutionId {}, appointmentId {}, appointmentStateId {}", institutionId, appointmentId, appointmentStateId);
		appointmentValidatorService.validateStateUpdate(institutionId, appointmentId, Short.parseShort(appointmentStateId), reason);
		boolean result = appointmentService.updateState(appointmentId, Short.parseShort(appointmentStateId), UserInfo.getCurrentAuditor(), reason);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
    }


    @GetMapping("/confirmed-appointment")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Boolean> hasNewConsultationEnabled(@PathVariable(name = "institutionId")  Integer institutionId,
                                                           @RequestParam(name = "patientId") Integer patientId){
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        Integer healthProfessionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        boolean result = disableValidation || enableNewConsultation || appointmentService.hasConfirmedAppointment(patientId, healthProfessionalId, dateTimeProvider.nowDate());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/consider-appointment")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Boolean> considerAppointment(@PathVariable(name = "institutionId")  Integer institutionId){
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        boolean result = !disableValidation && !enableNewConsultation;
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @PutMapping(value = "/{appointmentId}/update-phone-number")
    public ResponseEntity<Boolean> updatePhoneNumber(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "appointmentId") Integer appointmentId,
            @RequestParam(required = false) @Size(max = 20, message = "{appointment.new.phoneNumber.invalid}") String phoneNumber,
			@RequestParam(required = false) @Size(max = 10, message = "{appointment.new.phonePrefix.invalid}") String phonePrefix) {
        LOG.debug("Input parameters -> institutionId {},appointmentId {}, phonePrefix {}, phoneNumber {}", institutionId, appointmentId, phonePrefix, phoneNumber);
        boolean result = appointmentService.updatePhoneNumber(appointmentId, phonePrefix, phoneNumber,UserInfo.getCurrentAuditor());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @PutMapping(value = "/{appointmentId}/update-medical-coverage")
    public ResponseEntity<Boolean> updateMedicalCoverage(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "appointmentId") Integer appointmentId,
            @RequestParam(name = "patientMedicalCoverageId", required = false) Integer patientMedicalCoverageId) {
        LOG.debug("Input parameters -> institutionId {},appointmentId {}, patientMedicalCoverageId {}",
                institutionId, appointmentId, patientMedicalCoverageId);
        boolean result = appointmentService.updateMedicalCoverage(appointmentId, patientMedicalCoverageId);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
 
    @GetMapping("/getDailyAmounts")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
    public ResponseEntity<List<AppointmentDailyAmountDto>> getDailyAmounts(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestParam(name = "diaryId") String diaryId) {
        LOG.debug("Input parameters -> diaryId {}", diaryId);
        
        Integer diaryIdParam = Integer.parseInt(diaryId);

        Collection<AppointmentDailyAmountBo> resultService = appointmentDailyAmountService
                .getDailyAmounts(diaryIdParam);
        List<AppointmentDailyAmountDto> result = resultService.stream()
                .parallel()
                .map(appointmentMapper::toAppointmentDailyAmountDto)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{appointmentId}/notifyPatient")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    @ResponseStatus(HttpStatus.OK)
    public void notifyPatient(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "appointmentId") Integer appointmentId) {
        notifyPatient.run(institutionId, appointmentId);
    }

	private AppointmentBasicPatientDto toAppointmentBasicPatientDto(BasicPatientDto basicData, String phoneNumber, String phonePrefix) {
		BasicDataPersonDto basicPatientDto = basicData.getPerson();
		BasicPersonalDataDto basicPersonalDataDto = new BasicPersonalDataDto(basicPatientDto.getFirstName(), basicPatientDto.getLastName(), basicPatientDto.getIdentificationNumber(), basicPatientDto.getIdentificationTypeId(), phonePrefix, phoneNumber, basicPatientDto.getGender().getId(), basicPatientDto.getNameSelfDetermination());
		return new AppointmentBasicPatientDto(basicData.getId(), basicPersonalDataDto, basicData.getTypeId());
	}

	@GetMapping("/{patientId}/get-assigned-appointments")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<Collection<AssignedAppointmentDto>> getAssignedAppointmentsList(@PathVariable(name = "institutionId")  Integer institutionId,
																						  @PathVariable(name = "patientId") Integer patientId){
		LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		var result = appointmentService.getCompleteAssignedAppointmentInfo(patientId).stream()
				.map(appointmentAssigned ->
						(appointmentMapper.toAssignedAppointmentDto(appointmentAssigned)))
				.collect(Collectors.toList());
		LOG.debug("Result size {}", result.size());
		LOG.trace(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

}
