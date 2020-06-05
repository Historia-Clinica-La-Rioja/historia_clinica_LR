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
import net.pladema.internation.repository.documents.entity.Document;
import net.pladema.internation.repository.documents.entity.InternmentEpisode;
import net.pladema.internation.repository.documents.entity.PatientDischarge;
import net.pladema.internation.repository.internment.InternmentEpisodeRepository;
import net.pladema.internation.repository.masterdata.entity.InternmentEpisodeStatus;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.internment.ResponsibleContactService;
import net.pladema.internation.service.internment.summary.domain.InternmentSummaryBo;
import net.pladema.sgx.exceptions.NotFoundException;
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

	public InternmentEpisodeController(InternmentEpisodeService internmentEpisodeService,
									   HealthcareProfessionalExternalService healthcareProfessionalExternalService,
									   InternmentEpisodeMapper internmentEpisodeMapper, BedExternalService bedExternalService,
									   PatientDischargeMapper patientDischargeMapper, ResponsibleContactService responsibleContactService,
									   InternmentEpisodeRepository internmentEpisodeRepository, FeatureFlagsService featureFlagsService,
									   DocumentRepository documentRepository) {
		this.internmentEpisodeService = internmentEpisodeService;
		this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
		this.internmentEpisodeMapper = internmentEpisodeMapper;
		this.bedExternalService = bedExternalService;
		this.patientDischargeMapper = patientDischargeMapper;
		this.responsibleContactService = responsibleContactService;
		this.internmentEpisodeRepository = internmentEpisodeRepository;
		this.featureFlagsService = featureFlagsService;
		this.documentRepository = documentRepository;
	}

	@InternmentValid
	@GetMapping("/{internmentEpisodeId}/summary")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ADMINISTRATIVO, ENFERMERO_ADULTO_MAYOR')")
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
	@PostMapping("/{internmentEpisodeId}/discharge")
	public ResponseEntity<PatientDischargeDto> dischargeInternmentEpisode(
			@InternmentDischargeValid @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody PatientDischargeDto patientDischargeDto) {
		PatientDischarge patientDischarge = patientDischargeMapper.toPatientDischarge(patientDischargeDto);
		InternmentSummaryBo internmentEpisodeSummary = internmentEpisodeService.getIntermentSummary(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("bad-episode-id", "Internment episode not found"));
		patientDischarge.setInternmentEpisodeId(internmentEpisodeId);
		PatientDischarge patientDischageSaved = internmentEpisodeService.addPatientDischarge(patientDischarge);
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
		if (this.featureFlagsService.isOn("habilitarAltaSinEpicrisis")) {
			return ResponseEntity.ok(internmentEpisode.getEntryDate());
		}
		Document epicrisis = documentRepository.findById(internmentEpisode.getEpicrisisDocId())
				.orElseThrow(() -> new NotFoundException("bad-episode-id", "Epicrisis needed for discharge"));
		LocalDate minDischargeDate = epicrisis.getCreatedOn().toLocalDate();
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