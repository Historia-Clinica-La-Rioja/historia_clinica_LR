package net.pladema.clinichistory.hospitalization.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentDischargeValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.ProbableDischargeDateValid;
import net.pladema.clinichistory.hospitalization.controller.dto.*;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.InternmentEpisodeBMDto;
import net.pladema.clinichistory.hospitalization.controller.mapper.InternmentEpisodeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.PatientDischargeMapper;
import net.pladema.clinichistory.hospitalization.controller.mapper.ResponsibleContactMapper;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import net.pladema.clinichistory.hospitalization.service.patientdischarge.PatientDischargeService;
import net.pladema.clinichistory.ips.repository.masterdata.entity.InternmentEpisodeStatus;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.sgx.featureflags.AppFeature;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/institutions/{institutionId}/internments")
@Api(value = "Internment Episode", tags = { "Internment Episode" })
@Validated
public class InternmentEpisodeController {

	private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeController.class);

	public static final String INTERNMENT_NOT_FOUND = "internmentepisode.not.found";

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

	public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService,
									   HealthcareProfessionalExternalService healthcareProfessionalExternalService,
									   InternmentEpisodeMapper internmentEpisodeMapper,
									   BedExternalService bedExternalService,
									   PatientDischargeMapper patientDischargeMapper,
									   ResponsibleContactService responsibleContactService,
									   FeatureFlagsService featureFlagsService,
									   PatientDischargeService patientDischargeService,
									   ResponsibleContactMapper responsibleContactMapper, LocalDateMapper localDateMapper) {
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
	}

	@InternmentValid
	@GetMapping("/{internmentEpisodeId}/summary")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ADMINISTRATIVO, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
	public ResponseEntity<InternmentSummaryDto> internmentEpisodeSummary(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug("Input parameters -> {}", internmentEpisodeId);
		InternmentSummaryBo internmentSummaryBo = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElse(new InternmentSummaryBo());
		InternmentSummaryDto result = internmentEpisodeMapper.toInternmentSummaryDto(internmentSummaryBo);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	@Transactional
    public ResponseEntity<InternmentEpisodeDto> addInternmentEpisode(
            @PathVariable(name = "institutionId") Integer institutionId,
            @Valid @RequestBody InternmentEpisodeADto internmentEpisodeADto) {
        InternmentEpisode internmentEpisodeToSave = internmentEpisodeMapper.toInternmentEpisode(internmentEpisodeADto);
        internmentEpisodeToSave = internmentEpisodeService.addInternmentEpisode(internmentEpisodeToSave, institutionId);
        InternmentEpisodeDto result = internmentEpisodeMapper.toInternmentEpisodeDto(internmentEpisodeToSave);
        bedExternalService.updateBedStatusOccupied(internmentEpisodeADto.getBedId());
        if (internmentEpisodeADto.getResponsibleDoctorId() != null)
        	healthcareProfessionalExternalService.addHealthcareProfessionalGroup(result.getId(), internmentEpisodeADto.getResponsibleDoctorId());
		responsibleContactService.addResponsibleContact(responsibleContactMapper.toResponsibleContactBo(internmentEpisodeADto.getResponsibleContact()), result.getId());
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

	@Transactional
	@PostMapping("/{internmentEpisodeId}/medicaldischarge")
	public ResponseEntity<PatientDischargeDto> medicalDischargeInternmentEpisode(
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischageSaved = internmentEpisodeService.savePatientDischarge(patientDischarge);
		return ResponseEntity.ok(patientDischargeMapper.toPatientDischargeDto(patientDischageSaved));
	}
    
	@Transactional
	@PostMapping("/{internmentEpisodeId}/administrativedischarge")
	public ResponseEntity<PatientDischargeDto> dischargeInternmentEpisode(
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischageSaved = internmentEpisodeService.savePatientDischarge(patientDischarge);
		bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		internmentEpisodeService.updateInternmentEpisodeSatus(internmentEpisodeId,
				Short.valueOf(InternmentEpisodeStatus.INACTIVE));
		return ResponseEntity.ok(patientDischargeMapper.toPatientDischargeDto(patientDischageSaved));
	}

	@GetMapping("/{internmentEpisodeId}/minDischargeDate")
	public ResponseEntity<LocalDate> getMinDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
		if (this.featureFlagsService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
			return ResponseEntity.ok(internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId));
		}
		PatientDischargeBo patientDischarge =  patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		LocalDate minDischargeDate = patientDischarge.getMedicalDischargeDate();
		return ResponseEntity.ok(minDischargeDate);
	}

	@GetMapping("/{internmentEpisodeId}")
	public ResponseEntity<InternmentEpisodeBMDto> getInternmentEpisode(
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "institutionId") Integer institutionId) {
		InternmentEpisode internmentEpisode = internmentEpisodeService.getInternmentEpisode(internmentEpisodeId,institutionId);
		return ResponseEntity.ok(internmentEpisodeMapper.toInternmentEpisodeBMDto(internmentEpisode));
	}

	@GetMapping("/{internmentEpisodeId}/patientdischarge")
	public ResponseEntity<PatientDischargeDto> getPatientDischarge(
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		PatientDischargeBo pdbo =	patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(pdbo);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/lastupdatedate")
	public ResponseEntity<LocalDate> getLastUpdateDateOfInternmentEpisode(
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		return ResponseEntity.ok(internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId));
	}

	@ProbableDischargeDateValid
	@PutMapping("/{internmentEpisodeId}/probabledischargedate")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
	public ResponseEntity<LocalDateTime> updateProbableDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody ProbableDischargeDateDto probableDischargeDateDto) {
		LOG.debug("Input parameters -> institutionId {}, intermentEpisodeId {} ", institutionId, internmentEpisodeId);
		LocalDateTime probableDischargeDate = localDateMapper.fromStringToLocalDateTime(probableDischargeDateDto.getProbableDischargeDate());
		LocalDateTime result = internmentEpisodeService.updateInternmentEpisodeProbableDischargeDate(internmentEpisodeId, probableDischargeDate);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok(result);
	}
}