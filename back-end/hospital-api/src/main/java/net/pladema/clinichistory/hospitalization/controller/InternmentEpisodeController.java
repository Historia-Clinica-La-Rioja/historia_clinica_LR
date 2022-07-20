package net.pladema.clinichistory.hospitalization.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentDischargeValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentPhysicalDischargeValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.ProbableDischargeDateValid;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeADto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentSummaryDto;
import net.pladema.clinichistory.hospitalization.controller.dto.PatientDischargeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.ProbableDischargeDateDto;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.InternmentEpisodeBMDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.PatientDischargeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleContactMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import net.pladema.clinichistory.hospitalization.service.patientdischarge.PatientDischargeService;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@RestController
@RequestMapping("/institutions/{institutionId}/internments")
@Tag(name = "Internment Episode", description = "Internment Episode")
@Validated
public class InternmentEpisodeController {

	private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeController.class);

	public static final String INTERNMENT_NOT_FOUND = "internmentepisode.not.found";
	public static final String OUTPUT = "Output -> {}";
	public static final String INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID = "Input parameters -> institutionId {}, internmentEpisodeId {} ";

	private final InternmentEpisodeService internmentEpisodeService;

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final InternmentEpisodeMapper internmentEpisodeMapper;

	private final PatientDischargeMapper patientDischargeMapper;

	private final BedExternalService bedExternalService;

	private final ResponsibleContactService responsibleContactService;

	private final FeatureFlagsService featureFlagsService;

	private final PatientDischargeService patientDischargeService;

	private final ResponsibleContactMapper responsibleContactMapper;

	private final LocalDateMapper localDateMapper;

	private final HospitalApiPublisher hospitalApiPublisher;

	public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService, HealthcareProfessionalExternalService healthcareProfessionalExternalService, InternmentEpisodeMapper internmentEpisodeMapper, BedExternalService bedExternalService, PatientDischargeMapper patientDischargeMapper, ResponsibleContactService responsibleContactService, FeatureFlagsService featureFlagsService, PatientDischargeService patientDischargeService, ResponsibleContactMapper responsibleContactMapper, LocalDateMapper localDateMapper, HospitalApiPublisher hospitalApiPublisher) {
		this.internmentEpisodeService = internmentEpisodeService;
		this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
		this.internmentEpisodeMapper = internmentEpisodeMapper;
		this.bedExternalService = bedExternalService;
		this.patientDischargeMapper = patientDischargeMapper;
		this.responsibleContactService = responsibleContactService;
		this.featureFlagsService = featureFlagsService;
		this.patientDischargeService = patientDischargeService;
		this.responsibleContactMapper = responsibleContactMapper;
		this.localDateMapper = localDateMapper;
		this.hospitalApiPublisher = hospitalApiPublisher;
	}

	@InternmentValid
	@GetMapping("/{internmentEpisodeId}/summary")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ADMINISTRADOR_DE_CAMAS, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<InternmentSummaryDto> internmentEpisodeSummary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug("Input parameters -> {}", internmentEpisodeId);
		InternmentSummaryBo internmentSummaryBo = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElse(new InternmentSummaryBo());
		InternmentSummaryDto result = internmentEpisodeMapper.toInternmentSummaryDto(internmentSummaryBo);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok().body(result);
	}

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	@Transactional
    public ResponseEntity<InternmentEpisodeDto> addInternmentEpisode(
            @PathVariable(name = "institutionId") Integer institutionId,
            @Valid @RequestBody InternmentEpisodeADto internmentEpisodeADto) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeADto {} ", institutionId, internmentEpisodeADto);
        InternmentEpisode internmentEpisodeToSave = internmentEpisodeMapper.toInternmentEpisode(internmentEpisodeADto);
        internmentEpisodeToSave = internmentEpisodeService.addInternmentEpisode(internmentEpisodeToSave, institutionId);
        InternmentEpisodeDto result = internmentEpisodeMapper.toInternmentEpisodeDto(internmentEpisodeToSave);
        bedExternalService.updateBedStatusOccupied(internmentEpisodeADto.getBedId());
        if (internmentEpisodeADto.getResponsibleDoctorId() != null)
        	healthcareProfessionalExternalService.addHealthcareProfessionalGroup(result.getId(), internmentEpisodeADto.getResponsibleDoctorId());
		responsibleContactService.addResponsibleContact(responsibleContactMapper.toResponsibleContactBo(internmentEpisodeADto.getResponsibleContact()), result.getId());
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	@PostMapping("/{internmentEpisodeId}/medicaldischarge")
	public ResponseEntity<PatientDischargeDto> medicalDischargeInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, PatientDischargeDto {} ", internmentEpisodeId, patientDischargeDto);
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.saveMedicalDischarge(patientDischarge);
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		internmentEpisodeService.getPatient(patientDischargeSaved.getInternmentEpisodeId())
				.ifPresent( patientId -> hospitalApiPublisher.publish(patientId, institutionId,EHospitalApiTopicDto.ALTA_MEDICA) );
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	@PostMapping("/{internmentEpisodeId}/administrativedischarge")
	public ResponseEntity<PatientDischargeDto> dischargeInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, PatientDischargeDto {} ", internmentEpisodeId, patientDischargeDto);
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		patientDischarge.setMedicalDischargeDate(internmentEpisodeSummary.getMedicalDischargeDate());
		if (!internmentEpisodeSummary.freeBed())
			patientDischarge.setPhysicalDischargeDate(patientDischarge.getAdministrativeDischargeDate());
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.saveAdministrativeDischarge(patientDischarge);
		if (!internmentEpisodeSummary.freeBed())
			bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		internmentEpisodeService.updateInternmentEpisodeStatus(internmentEpisodeId,
				Short.valueOf(InternmentEpisodeStatus.INACTIVE));
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@Transactional
	@PostMapping("/{internmentEpisodeId}/physicaldischarge")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<PatientDischargeDto> physicalDischargeInternmentEpisode(
			@PathVariable(name="institutionId") Integer institutionId,
			@InternmentPhysicalDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug("Input parameters -> internmentEpisodeId {} ", internmentEpisodeId);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.savePatientPhysicalDischarge(internmentEpisodeId);
		bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		internmentEpisodeService.getPatient(patientDischargeSaved.getInternmentEpisodeId())
				.ifPresent( patientId -> hospitalApiPublisher.publish(patientId, institutionId, EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__DISCHARGE__PHYSIC) );
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/minDischargeDate")
	public DateTimeDto getMinDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
		LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		if (this.featureFlagsService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
			return localDateMapper.toDateTimeDto(internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId));
		}
		PatientDischargeBo patientDischarge =  patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		LocalDateTime result = patientDischarge.getMedicalDischargeDate();
		LOG.debug(OUTPUT, result);
		return localDateMapper.toDateTimeDto(result);
	}

	@GetMapping("/{internmentEpisodeId}")
	public ResponseEntity<InternmentEpisodeBMDto> getInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		InternmentEpisode internmentEpisode = internmentEpisodeService.getInternmentEpisode(internmentEpisodeId,institutionId);
		InternmentEpisodeBMDto result = internmentEpisodeMapper.toInternmentEpisodeBMDto(internmentEpisode);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/patientdischarge")
	public ResponseEntity<PatientDischargeDto> getPatientDischarge(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		PatientDischargeBo patientDischargeBo =	patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeBo);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/lastupdatedate")
	public DateTimeDto getLastUpdateDateOfInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		LocalDateTime result = internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId);
		LOG.debug(OUTPUT, result);
		return localDateMapper.toDateTimeDto(result);
	}

	@ProbableDischargeDateValid
	@PutMapping("/{internmentEpisodeId}/probabledischargedate")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<DateTimeDto> updateProbableDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody ProbableDischargeDateDto probableDischargeDateDto) {
		LOG.debug("Input parameters -> institutionId {}, intermentEpisodeId {} ", institutionId, internmentEpisodeId);
		LocalDateTime probableDischargeDate = localDateMapper.fromStringToLocalDateTime(probableDischargeDateDto.getProbableDischargeDate());
		if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA))
			return new ResponseEntity<>(localDateMapper.toDateTimeDto(probableDischargeDate), HttpStatus.BAD_REQUEST);
		LocalDateTime result = internmentEpisodeService.updateInternmentEpisodeProbableDischargeDate(internmentEpisodeId, probableDischargeDate);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(localDateMapper.toDateTimeDto(result));


	}

}