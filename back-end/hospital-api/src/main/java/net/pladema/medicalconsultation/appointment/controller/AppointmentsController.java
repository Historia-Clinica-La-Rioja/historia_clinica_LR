package net.pladema.medicalconsultation.appointment.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointment;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentDiary;
import net.pladema.medicalconsultation.appointment.controller.constraints.ValidAppointmentState;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentListDto;
import net.pladema.medicalconsultation.appointment.controller.dto.CreateAppointmentDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.AppointmentValidatorService;
import net.pladema.medicalconsultation.appointment.service.CreateAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.patient.controller.dto.HealthInsurancePatientDataDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.sgx.security.utils.UserInfo;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointments")
@Api(value = "Appointments ", tags = { "Appointments" })
@Validated
public class AppointmentsController {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentsController.class);

    public static final String OUTPUT = "Output -> {}";

    private final AppointmentService appointmentService;
    
    private final AppointmentValidatorService appointmentValidatorService;

    private final CreateAppointmentService createAppointmentService;

    private final AppointmentMapper appointmentMapper;

    private final PatientExternalService patientExternalService;

    public AppointmentsController(AppointmentService appointmentService,
    							  AppointmentValidatorService appointmentValidatorService,
                                  CreateAppointmentService createAppointmentService,
                                  AppointmentMapper appointmentMapper,
                                  PatientExternalService patientExternalService) {
        super();
        this.appointmentService = appointmentService;
        this.appointmentValidatorService = appointmentValidatorService;
        this.createAppointmentService = createAppointmentService;
        this.appointmentMapper = appointmentMapper;
        this.patientExternalService = patientExternalService;
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
    @ValidAppointment
    public ResponseEntity<Integer> create(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody CreateAppointmentDto createAppointmentDto) {
        LOG.debug("Input parameters -> institutionId {}, appointmentDto {}", institutionId, createAppointmentDto);
        AppointmentBo newAppointmentBo = appointmentMapper.toAppointmentBo(createAppointmentDto);
        newAppointmentBo = createAppointmentService.execute(newAppointmentBo);
        Integer result = newAppointmentBo.getId();
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping(value = "/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ADMINISTRADOR_AGENDA, ENFERMERO')")
	public ResponseEntity<AppointmentDto> get(@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId) {
		LOG.debug("Input parameters -> institutionId {}, appointmentId {}", institutionId, appointmentId);
		Optional<AppointmentBo> resultService = appointmentService.getAppointment(appointmentId);
		Optional<AppointmentDto> result = resultService.map(appointmentMapper::toAppointmentDto);
		LOG.debug(OUTPUT, result);
		if (!result.isPresent())
			return ResponseEntity.noContent().build();
		return ResponseEntity.ok(result.get());
	}


    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ADMINISTRADOR_AGENDA, ENFERMERO')")
    public ResponseEntity<Collection<AppointmentListDto>> getList(@PathVariable(name = "institutionId")  Integer institutionId,
                                                                  @RequestParam(name = "diaryIds") @NotEmpty List<Integer> diaryIds){
        LOG.debug("Input parameters -> institutionId {}, diaryIds {}", institutionId, diaryIds);
        Collection<AppointmentBo> resultService = appointmentService.getAppointmentsByDiaries(diaryIds);
        Collection<AppointmentListDto> result = resultService.stream()
                .parallel()
                .map(this::mapData)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok(result);
    }

    private AppointmentListDto mapData(AppointmentBo appointmentBo) {
        LOG.debug("Input parameters -> appointmentBo {}", appointmentBo);
        HealthInsurancePatientDataDto healthInsurancePatientDataDto = patientExternalService.getHealthInsurancePatientData(appointmentBo.getPatientId());
        AppointmentListDto result = appointmentMapper.toAppointmentListDto(appointmentBo, healthInsurancePatientDataDto);
        LOG.debug(OUTPUT, result);
        return result;
    }


    @Transactional
    @PutMapping(value = "/{appointmentId}/change-state")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
    public ResponseEntity<Boolean> changeState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @ValidAppointmentDiary @PathVariable(name = "appointmentId") Integer appointmentId,
            @ValidAppointmentState @RequestParam(name = "appointmentStateId") String appointmentStateId,
            @RequestParam(name = "reason", required = false) String reason) {
		LOG.debug("Input parameters -> institutionId {}, appointmentId {}, appointmentStateId {}", institutionId, appointmentId, appointmentStateId);
		appointmentValidatorService.validateStateUpdate(appointmentId, Short.parseShort(appointmentStateId), reason);
		boolean result = appointmentService.updateState(appointmentId, Short.parseShort(appointmentStateId), UserInfo.getCurrentAuditor(), reason);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
    }
}
