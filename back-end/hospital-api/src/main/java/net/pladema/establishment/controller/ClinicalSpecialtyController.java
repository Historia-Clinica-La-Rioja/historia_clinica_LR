package net.pladema.establishment.controller;

import java.util.List;

import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.staff.controller.dto.ProfessionalsByClinicalSpecialtyDto;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.service.ClinicalSpecialtyService;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;

@RestController
@Tag(name = "Clinical specialties", description = "Clinical specialties")
public class ClinicalSpecialtyController {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtyController.class);

    private final ClinicalSpecialtyRepository clinicalSpecialtyRepository;
    private final ClinicalSpecialtyService clinicalSpecialtyService;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final AppointmentService appointmentService;
    private final DateTimeProvider dateTimeProvider;
    private final ClinicalSpecialtyMapper clinicalSpecialtyMapper;
    private final HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;

    public ClinicalSpecialtyController(ClinicalSpecialtyRepository clinicalSpecialtyRepository,
                                       ClinicalSpecialtyService clinicalSpecialtyService,
                                       HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService,
                                       HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                       AppointmentService appointmentService,
                                       DateTimeProvider dateTimeProvider,
                                       ClinicalSpecialtyMapper clinicalSpecialtyMapper) {
        this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
        this.clinicalSpecialtyService = clinicalSpecialtyService;
        this.healthcareProfessionalSpecialtyService = healthcareProfessionalSpecialtyService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.appointmentService = appointmentService;
        this.dateTimeProvider = dateTimeProvider;
        this.clinicalSpecialtyMapper = clinicalSpecialtyMapper;
    }

    @GetMapping("/institution/{institutionId}/clinicalspecialty/professional/{professionalId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<List<ClinicalSpecialtyDto>> getAllSpecialtyByProfessional(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "professionalId") Integer professionalId) {
        var clinicalSpecialties = clinicalSpecialtyService.getSpecialtiesByProfessional(professionalId);
        var result = clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(clinicalSpecialties);
        LOG.debug("Get all Clinical Specialty by Professional {} and Institution {} => {}", professionalId, institutionId,
                result);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/institution/{institutionId}/clinicalspecialty/professional", params = "professionalsIds")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA')")
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
    
    @GetMapping("/institution/{institutionId}/clinicalspecialty/patient/{patientId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<ClinicalSpecialtyDto> getAppointmentSpecialty(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId) {

        Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Integer appointmentId = appointmentService.getAppointmentsId(patientId, professionalId, dateTimeProvider.nowDate()).get(0);
        AppointmentBo appointment = appointmentService.getAppointment(appointmentId).orElse(new AppointmentBo());
        ClinicalSpecialty clinicalSpecialty = clinicalSpecialtyRepository.getClinicalSpecialtyByDiary(appointment.getDiaryId());
        clinicalSpecialty.fixSpecialtyType();
        LOG.debug("Get all Clinical Specialty by Institution {} and Patient {} => {}", institutionId,
                patientId, clinicalSpecialty);
        ClinicalSpecialtyDto result = new ClinicalSpecialtyDto();
        result.setId(clinicalSpecialty.getId());
        result.setName(clinicalSpecialty.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/institution/{institutionId}/clinicalspecialty/loggedProfessionalClinicalSpecialty")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<ClinicalSpecialtyDto>> getLoggedInProfessionalClinicalSpecialties(
            @PathVariable(name = "institutionId") Integer institutionId) {
        Integer professionalId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        var clinicalSpecialties = clinicalSpecialtyService.getSpecialtiesByProfessional(professionalId);
        var result = clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(clinicalSpecialties);
        LOG.debug("Get all Clinical Specialty by Professional {} and Institution {} => {}", professionalId, institutionId,
                result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/clinicalSpecialty")
    public ResponseEntity<List<ClinicalSpecialtyDto>> getAll() {
        LOG.debug("No input parameters");
        List<ClinicalSpecialtyDto> result = clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(clinicalSpecialtyService.getAll());
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

	@GetMapping(value = "/institution/{institutionId}/clinicalspecialty/diary/professional", params = "professionalsIds")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<List<ProfessionalsByClinicalSpecialtyDto>> getManyByActiveDiariesAndProfessionals(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam List<Integer> professionalsIds) {
		List<ProfessionalsByClinicalSpecialtyBo> clinicalSpecialties =
				this.healthcareProfessionalSpecialtyService.getProfessionalsByActiveDiaryAndClinicalSpecialtyBo(professionalsIds, institutionId);
		List<ProfessionalsByClinicalSpecialtyDto> professionalsByClinicalSpecialtyDtos =
				clinicalSpecialtyMapper.fromProfessionalsByClinicalSpecialtyBoList(clinicalSpecialties);

		LOG.debug("Get all Clinical Specialty by active diaries, Professionals {} and Institution {} => {}", professionalsIds, institutionId, professionalsByClinicalSpecialtyDtos);
		return ResponseEntity.ok(professionalsByClinicalSpecialtyDtos);
	}

	@GetMapping(value = "/institution/{institutionId}/clinicalspecialty/by-destination-institution")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<ClinicalSpecialtyDto>> getAllByInstitutionIdAndActiveDiaries(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam Integer destinationInstitutionId) {
		LOG.debug("Input parameters -> institutionId {} and destinationInstitutionId {}", institutionId, destinationInstitutionId);
		List<ClinicalSpecialtyDto> clinicalSpecialties = clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(clinicalSpecialtyService.getAllByInstitutionIdAndActiveDiaries(destinationInstitutionId));
		LOG.debug("Get all Clinical Specialty by active diaries and institution {} => {}", destinationInstitutionId);
		return ResponseEntity.ok(clinicalSpecialties);
	}

	@GetMapping("/institution/{institutionId}/clinicalspecialty/careline/{careLineId}/destinationinstitution/{destinationInstitutionId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<List<ClinicalSpecialtyDto>> getAllByDestinationInstitution(@PathVariable(name = "institutionId") Integer institutionId,
																					 @PathVariable(name= "careLineId") Integer careLineId,
																					 @PathVariable(name= "destinationInstitutionId") Integer destinationInstitutionId) {
		List<ClinicalSpecialtyBo> clinicalSpecialties = clinicalSpecialtyService.getClinicalSpecialtiesByCareLineIdAndDestinationIntitutionId(careLineId, destinationInstitutionId);
		LOG.debug("Get all Clinical Specialties by CareLine and Destination Institution {} => {}", careLineId, destinationInstitutionId, clinicalSpecialties);
		return ResponseEntity.ok(clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(clinicalSpecialties));
	}


}

