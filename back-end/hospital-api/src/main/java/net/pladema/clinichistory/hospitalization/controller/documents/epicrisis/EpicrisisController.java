package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis;

import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.FetchHospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState.HospitalizationGeneralState;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.DeleteEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis")
@Tag(name = "Epicrisis", description = "Epicrisis")
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
public class EpicrisisController {

    private static final Logger LOG = LoggerFactory.getLogger(EpicrisisController.class);

    public static final String OUTPUT = "Output -> {}";
    
    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEpicrisisService createEpicrisisService;

    private final EpicrisisService epicrisisService;

    private final EpicrisisMapper epicrisisMapper;

    private final FetchHospitalizationGeneralState fetchHospitalizationGeneralState;

    private final PatientExternalService patientExternalService;

    private final DeleteEpicrisisService deleteEpicrisisService;

	private final UpdateEpicrisisService updateEpicrisisService;

    public EpicrisisController(
			InternmentEpisodeService internmentEpisodeService,
			CreateEpicrisisService createEpicrisisService,
			EpicrisisService epicrisisService,
			EpicrisisMapper epicrisisMapper,
			FetchHospitalizationGeneralState fetchHospitalizationGeneralState,
			PatientExternalService patientExternalService,
			DeleteEpicrisisService deleteEpicrisisService,
			UpdateEpicrisisService updateEpicrisisService
    ) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEpicrisisService = createEpicrisisService;
        this.epicrisisService = epicrisisService;
        this.epicrisisMapper = epicrisisMapper;
        this.fetchHospitalizationGeneralState = fetchHospitalizationGeneralState;
        this.patientExternalService = patientExternalService;
        this.deleteEpicrisisService = deleteEpicrisisService;
		this.updateEpicrisisService = updateEpicrisisService;
    }


    @PostMapping
    public ResponseEntity<Boolean> createDocument(
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
        createEpicrisisService.execute(epicrisis, false);

        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }

	@GetMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> getDocument(
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

    @GetMapping("/general")
    @InternmentValid
    public ResponseEntity<EpicrisisGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        HospitalizationGeneralState interment = fetchHospitalizationGeneralState.getInternmentGeneralState(internmentEpisodeId);
        EpicrisisGeneralStateDto result = epicrisisMapper.toEpicrisisGeneralStateDto(interment);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

	@DeleteMapping("/{epicrisisId}")
	public ResponseEntity<Boolean> deleteEpicrisis(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "epicrisisId") Long epicrisisId,
			@RequestBody String reason) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, reason {}",
				institutionId, internmentEpisodeId, epicrisisId, reason);
		deleteEpicrisisService.execute(internmentEpisodeId, epicrisisId, reason);
		LOG.debug(OUTPUT, Boolean.TRUE);
		return  ResponseEntity.ok().body(Boolean.TRUE);
	}

	@PutMapping("/{epicrisisId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
	public ResponseEntity<Long> updateEpicrisis(
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
		Long result = updateEpicrisisService.execute(internmentEpisodeId, epicrisisId, newEpicrisis);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

	@GetMapping("/existUpdates")
	@InternmentValid
	public ResponseEntity<Boolean> existUpdatesAfterEpicrisis(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);

		Boolean result = internmentEpisodeService.haveUpdatesAfterEpicrisis(internmentEpisodeId);

		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

    
}