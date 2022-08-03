package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CloseEpicrisisDraftService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisDraftService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;

import net.pladema.patient.controller.service.PatientExternalService;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis/draft")
@Tag(name = "Epicrisis Draft", description = "Epicrisis Draft")
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
public class EpicrisisDraftController {

	private static final Logger LOG = LoggerFactory.getLogger(EpicrisisDraftController.class);

	public static final String OUTPUT = "Output -> {}";

	private final InternmentEpisodeService internmentEpisodeService;

	private final CreateEpicrisisService createEpicrisisService;

	private final EpicrisisService epicrisisService;

	private final EpicrisisMapper epicrisisMapper;

	private final PatientExternalService patientExternalService;

	private final UpdateEpicrisisDraftService updateEpicrisisDraftService;

	private final CloseEpicrisisDraftService closeEpicrisisDraftService;

	public EpicrisisDraftController(
			InternmentEpisodeService internmentEpisodeService,
			CreateEpicrisisService createEpicrisisService,
			EpicrisisService epicrisisService,
			EpicrisisMapper epicrisisMapper,
			PatientExternalService patientExternalService,
			UpdateEpicrisisDraftService updateEpicrisisDraftService,
			CloseEpicrisisDraftService closeEpicrisisDraftService
	) {
		this.internmentEpisodeService = internmentEpisodeService;
		this.createEpicrisisService = createEpicrisisService;
		this.epicrisisService = epicrisisService;
		this.epicrisisMapper = epicrisisMapper;
		this.patientExternalService = patientExternalService;
		this.updateEpicrisisDraftService = updateEpicrisisDraftService;
		this.closeEpicrisisDraftService = closeEpicrisisDraftService;
	}


	@PostMapping
	public ResponseEntity<Boolean> createDraft(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@RequestBody EpicrisisDto epicrisisDto) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisis {}",
				institutionId, internmentEpisodeId, epicrisisDto);
		EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(epicrisis::setPatientInfo,() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		epicrisis.setEncounterId(internmentEpisodeId);
		epicrisis.setInstitutionId(institutionId);
		createEpicrisisService.execute(epicrisis, true);

		LOG.debug(OUTPUT, Boolean.TRUE);
		return  ResponseEntity.ok().body(Boolean.TRUE);
	}

	@GetMapping("/{epicrisisId}")
	@InternmentValid
	@DocumentValid(isConfirmed= false, documentType = DocumentType.EPICRISIS)
	public ResponseEntity<ResponseEpicrisisDto> getDraft(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "epicrisisId") Long epicrisisId){
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}",
				institutionId, internmentEpisodeId, epicrisisId);
		EpicrisisBo epicrisis = epicrisisService.getDocument(epicrisisId);
		ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	@PutMapping("/{epicrisisId}")
	public ResponseEntity<Long> updateDraft(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "epicrisisId") Long epicrisisId,
			@RequestBody EpicrisisDto epicrisisDto) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, newEpicrisis {}",
				institutionId, internmentEpisodeId, epicrisisId, epicrisisDto);
		EpicrisisBo newEpicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(newEpicrisis::setPatientInfo, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		newEpicrisis.setPatientId(newEpicrisis.getPatientInfo().getId());
		newEpicrisis.setInstitutionId(institutionId);
		newEpicrisis.setEncounterId(internmentEpisodeId);
		Long result = updateEpicrisisDraftService.run(internmentEpisodeId, epicrisisId, newEpicrisis);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	@PutMapping("/final/{epicrisisId}")
	public ResponseEntity<Long> closeDraft(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "epicrisisId") Long epicrisisId,
			@RequestBody EpicrisisDto epicrisisDto) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, newEpicrisis {}",
				institutionId, internmentEpisodeId, epicrisisId, epicrisisDto);
		EpicrisisBo newEpicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(newEpicrisis::setPatientInfo, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		newEpicrisis.setInstitutionId(institutionId);
		newEpicrisis.setEncounterId(internmentEpisodeId);
		Long result = closeEpicrisisDraftService.execute(internmentEpisodeId, epicrisisId, newEpicrisis);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}


}
