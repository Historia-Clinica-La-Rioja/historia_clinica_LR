package net.pladema.establishment.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.hospitalization.controller.dto.ClinicalSpecialtyDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.dto.ProfessionalsByClinicalSpecialtyDto;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "ClinicalSpecialty", tags = { "Clinical Specialty" })
@RequestMapping("/institution/{institutionId}/clinicalspecialty")
public class ClinicalSpecialtyController {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtyController.class);

    private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final AppointmentService appointmentService;
    private final DateTimeProvider dateTimeProvider;
    private final ClinicalSpecialtyMapper clinicalSpecialtyMapper;
    private final HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;

    public ClinicalSpecialtyController(ClinicalSpecialtyRepository clinicalSpecialtyRepository,
                                       HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService,
                                       HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                       AppointmentService appointmentService,
                                       DateTimeProvider dateTimeProvider,
                                       ClinicalSpecialtyMapper clinicalSpecialtyMapper) {
        this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
        this.healthcareProfessionalSpecialtyService = healthcareProfessionalSpecialtyService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.appointmentService = appointmentService;
        this.dateTimeProvider = dateTimeProvider;
        this.clinicalSpecialtyMapper = clinicalSpecialtyMapper;
    }

    @GetMapping("/professional/{professionalId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<List<ClinicalSpecialty>> getAllSpecialtyByProfessional(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "professionalId") Integer professionalId) {
        List<ClinicalSpecialty> clinicalSpecialties = clinicalSpecialtyRepository
                .getAllByProfessional(professionalId);
        LOG.debug("Get all Clinical Specialty by Professional {} and Institution {} => {}", professionalId, institutionId,
                clinicalSpecialties);
        return ResponseEntity.ok(clinicalSpecialties);
    }

    @GetMapping(value = "/professional", params = "professionalsIds")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO')")
    public ResponseEntity<List<ProfessionalsByClinicalSpecialtyDto>> getManyByProfessionals(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestParam List<Integer> professionalsIds) {

        List<ProfessionalsByClinicalSpecialtyBo> clinicalSpecialties =
                this.healthcareProfessionalSpecialtyService.getProfessionalsByClinicalSpecialtyBo(professionalsIds);

        List<ProfessionalsByClinicalSpecialtyDto> professionalsByClinicalSpecialtyDtos =
                clinicalSpecialtyMapper.fromProfessionalsByClinicalSpecialtyBoList(clinicalSpecialties);

        LOG.debug("Get all Clinical Specialty by Professionals {} and Institution {} => {}", professionalsIds, institutionId,
                professionalsByClinicalSpecialtyDtos);
        return ResponseEntity.ok(professionalsByClinicalSpecialtyDtos);
    }
    
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<ClinicalSpecialtyDto> getAppointmentSpecialty(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {

        Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Integer appointmentId = appointmentService.getAppointmentsId(patientId, professionalId, dateTimeProvider.nowDate()).get(0);
        AppointmentBo appointment = appointmentService.getAppointment(appointmentId).orElse(new AppointmentBo());
        ClinicalSpecialty clinicalSpecialty = clinicalSpecialtyRepository.getClinicalSpecialtyByDiary(appointment.getDiaryId());
        LOG.debug("Get all Clinical Specialty by Institution {} and Patient {} => {}", institutionId,
                patientId, clinicalSpecialty);
        ClinicalSpecialtyDto result = new ClinicalSpecialtyDto();
        result.setId(clinicalSpecialty.getId());
        result.setName(clinicalSpecialty.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/loggedProfessionalClinicalSpecialty")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<List<ClinicalSpecialty>> getLoggedInProfessionalClinicalSpecialties(
            @PathVariable(name = "institutionId") Integer institutionId) {
        Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        List<ClinicalSpecialty> clinicalSpecialties = clinicalSpecialtyRepository
                .getAllByProfessional(professionalId);
        LOG.debug("Get all Clinical Specialty by Professional {} and Institution {} => {}", professionalId, institutionId,
                clinicalSpecialties);
        return ResponseEntity.ok(clinicalSpecialties);
    }

}

