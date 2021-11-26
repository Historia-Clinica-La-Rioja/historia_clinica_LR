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
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import net.pladema.clinichistory.hospitalization.service.patientdischarge.PatientDischargeService;
import net.pladema.establishment.controller.service.BedExternalService;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
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
	@PostMapping("/{internmentEpisodeId}/medicaldischarge")
	public ResponseEntity<PatientDischargeDto> medicalDischargeInternmentEpisode(
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, PatientDischargeDto {} ", internmentEpisodeId, patientDischargeDto);
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.savePatientDischarge(patientDischarge);
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}
    
	@Transactional
	@PostMapping("/{internmentEpisodeId}/administrativedischarge")
	public ResponseEntity<PatientDischargeDto> dischargeInternmentEpisode(
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, PatientDischargeDto {} ", internmentEpisodeId, patientDischargeDto);
		PatientDischargeBo patientDischarge = patientDischargeMapper.toPatientDischargeBo(patientDischargeDto);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischargeSaved = internmentEpisodeService.savePatientDischarge(patientDischarge);
		bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		internmentEpisodeService.updateInternmentEpisodeStatus(internmentEpisodeId,
				Short.valueOf(InternmentEpisodeStatus.INACTIVE));
		PatientDischargeDto result = patientDischargeMapper.toPatientDischargeDto(patientDischargeSaved);
		LOG.debug(OUTPUT, result);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{internmentEpisodeId}/minDischargeDate")
	public DateDto getMinDischargeDate(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
		LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		if (this.featureFlagsService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
			return localDateMapper.toDateDto(internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId));
		}
		PatientDischargeBo patientDischarge =  patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", INTERNMENT_NOT_FOUND));
		LocalDate result = patientDischarge.getMedicalDischargeDate();
		LOG.debug(OUTPUT, result);
		return localDateMapper.toDateDto(result);
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
	public DateDto getLastUpdateDateOfInternmentEpisode(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INSTITUTION_ID_INTERNMENT_EPISODE_ID, institutionId, internmentEpisodeId);
		LocalDate result = internmentEpisodeService.getLastUpdateDateOfInternmentEpisode(internmentEpisodeId);
		LOG.debug(OUTPUT, result);
		return localDateMapper.toDateDto(result);
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