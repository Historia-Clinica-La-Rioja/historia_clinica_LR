package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.internation.controller.constraints.InternmentDischargeValid;
import net.pladema.internation.controller.constraints.InternmentValid;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeADto;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.internment.dto.InternmentSummaryDto;
import net.pladema.internation.controller.internment.dto.PatientDischargeDto;
import net.pladema.internation.controller.internment.dto.summary.InternmentEpisodeBMDto;
import net.pladema.internation.controller.internment.mapper.InternmentEpisodeMapper;
import net.pladema.internation.controller.internment.mapper.PatientDischargeMapper;
import net.pladema.internation.repository.documents.DocumentRepository;
import net.pladema.internation.repository.documents.PatientDischargeRepository;
import net.pladema.internation.repository.documents.entity.InternmentEpisode;
import net.pladema.internation.repository.internment.InternmentEpisodeRepository;
import net.pladema.internation.repository.masterdata.entity.InternmentEpisodeStatus;
import net.pladema.internation.service.documents.PatientDischargeService;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.internment.ResponsibleContactService;
import net.pladema.internation.service.internment.summary.domain.InternmentSummaryBo;
import net.pladema.internation.service.internment.summary.domain.PatientDischargeBo;
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

@RestController
@RequestMapping("/institutions/{institutionId}/internments")
@Api(value = "Internment Episode", tags = { "Internment Episode" })
@Validated
public class InternmentEpisodeController {

	private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeController.class);

	private final InternmentEpisodeService internmentEpisodeService;

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final InternmentEpisodeMapper internmentEpisodeMapper;

	private final PatientDischargeMapper patientDischargeMapper;

	private final BedExternalService bedExternalService;

	private final ResponsibleContactService responsibleContactService;

	private final InternmentEpisodeRepository internmentEpisodeRepository;

	private final FeatureFlagsService featureFlagsService;

	private final DocumentRepository documentRepository;

	private final PatientDischargeService patientDischargeService;

	public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService,
									   HealthcareProfessionalExternalService healthcareProfessionalExternalService,
									   InternmentEpisodeMapper internmentEpisodeMapper, BedExternalService bedExternalService,
									   PatientDischargeMapper patientDischargeMapper, ResponsibleContactService responsibleContactService,
									   InternmentEpisodeRepository internmentEpisodeRepository, FeatureFlagsService featureFlagsService,
									   DocumentRepository documentRepository, PatientDischargeRepository patientDischargeRepository, PatientDischargeService patientDischargeService) {
		this.internmentEpisodeService = internmentEpisodeService;
		this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
		this.internmentEpisodeMapper = internmentEpisodeMapper;
		this.bedExternalService = bedExternalService;
		this.patientDischargeMapper = patientDischargeMapper;
		this.responsibleContactService = responsibleContactService;
		this.internmentEpisodeRepository = internmentEpisodeRepository;
		this.featureFlagsService = featureFlagsService;
		this.documentRepository = documentRepository;
		this.patientDischargeService = patientDischargeService;
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
		responsibleContactService.addResponsibleContact(internmentEpisodeADto.getResponsibleContact(), result.getId());
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
				.orElseThrow(() -> new NotFoundException("bad-episode-id", "Internment episode not found"));
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischargeBo patientDischageSaved = internmentEpisodeService.savePatientDischarge(patientDischarge);
		bedExternalService.freeBed(internmentEpisodeSummary.getBedId());
		internmentEpisodeService.updateInternmentEpisodeSatus(internmentEpisodeId,
				Short.valueOf(InternmentEpisodeStatus.INACTIVE));
		return ResponseEntity.ok(patientDischargeMapper.toPatientDischargeDto(patientDischageSaved));
	}

	@GetMapping("/{internmentEpisodeId}/minDischargeDate")
	public ResponseEntity<LocalDate> getMinDischargeDate(
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", "Internment episode not found"));
		if (this.featureFlagsService.isOn(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)) {
			return ResponseEntity.ok(internmentEpisode.getEntryDate());
		}
		PatientDischargeBo patientDischarge =  patientDischargeService.getPatientDischarge(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", "Epicrisis needed for discharge"));
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

}