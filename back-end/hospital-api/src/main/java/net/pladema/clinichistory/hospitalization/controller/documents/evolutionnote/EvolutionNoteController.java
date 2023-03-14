package net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.constraints.EvolutionNoteValid;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.EvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.ResponseEvolutionNoteDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.dto.evolutiondiagnosis.EvolutionDiagnosisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.evolutionnote.mapper.EvolutionNoteMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.DeleteEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionDiagnosesService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/evolutionNote")
@Tag(name = "Evolution notes", description = "Evolution notes")
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO_ADULTO_MAYOR, ENFERMERO')")
public class EvolutionNoteController {

    private static final Logger LOG = LoggerFactory.getLogger(EvolutionNoteController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_INTERNMENT_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEvolutionNoteService createEvolutionNoteService;

    private final EvolutionNoteService evolutionNoteService;

    private final EvolutionDiagnosesService evolutionDiagnosesService;

    private final EvolutionNoteMapper evolutionNoteMapper;

    private final PatientExternalService patientExternalService;

	private final DeleteEvolutionNoteService deleteEvolutionNoteService;

	private final UpdateEvolutionNoteService updateEvolutionNoteService;

    public EvolutionNoteController(InternmentEpisodeService internmentEpisodeService,
                                   CreateEvolutionNoteService createEvolutionNoteService,
                                   EvolutionNoteService evolutionNoteService,
                                   EvolutionDiagnosesService evolutionDiagnosesService,
                                   EvolutionNoteMapper evolutionNoteMapper,
                                   PatientExternalService patientExternalService,
								   DeleteEvolutionNoteService deleteEvolutionNoteService,
								   UpdateEvolutionNoteService updateEvolutionNoteService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEvolutionNoteService = createEvolutionNoteService;
        this.evolutionNoteService = evolutionNoteService;
        this.evolutionDiagnosesService = evolutionDiagnosesService;
        this.evolutionNoteMapper = evolutionNoteMapper;
        this.patientExternalService = patientExternalService;
        this.deleteEvolutionNoteService = deleteEvolutionNoteService;
		this.updateEvolutionNoteService = updateEvolutionNoteService;
    }

    @PostMapping
    public ResponseEntity<Boolean> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody EvolutionNoteDto evolutionNoteDto) {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNote {}",
                institutionId, internmentEpisodeId, evolutionNoteDto);
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
        internmentEpisodeService.getPatient(internmentEpisodeId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(evolutionNote::setPatientInfo,() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
        evolutionNote.setEncounterId(internmentEpisodeId);
        evolutionNote.setInstitutionId(institutionId);
        createEvolutionNoteService.execute(evolutionNote);
        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/evolutionDiagnosis")
    @InternmentValid
    @EvolutionNoteValid
    public ResponseEntity<Boolean> createEvolutionDiagnosis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EvolutionDiagnosisDto evolutionDiagnosisDto) {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionDiagnosisDto {}",
                institutionId, internmentEpisodeId, evolutionDiagnosisDto);
        Integer patientId = internmentEpisodeService.getPatient(internmentEpisodeId)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_INTERNMENT_EPISODE));
        EvolutionNoteBo evolutionNote = evolutionNoteMapper.fromEvolutionDiagnosisDto(evolutionDiagnosisDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(evolutionNote::setPatientInfo,() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		evolutionNote.setInstitutionId(institutionId);
		evolutionNote.setPatientId(patientId);
		evolutionNote.setEncounterId(internmentEpisodeId);
		evolutionNote.setIsNursingEvolutionNote(false);
        evolutionDiagnosesService.execute(evolutionNote);

        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }



    @GetMapping("/{evolutionNoteId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.EVALUATION_NOTE)
    public ResponseEntity<ResponseEvolutionNoteDto> getDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "evolutionNoteId") Long evolutionNoteId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
                institutionId, internmentEpisodeId, evolutionNoteId);
        EvolutionNoteBo evolutionNoteBo = evolutionNoteService.getDocument(evolutionNoteId);
        ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNoteBo);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

	@GetMapping("/nursing/{evolutionNoteId}")
	@InternmentValid
	@DocumentValid(isConfirmed = true, documentType = DocumentType.NURSING_EVOLUTION_NOTE)
	public ResponseEntity<ResponseEvolutionNoteDto> getNursingDocument(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "evolutionNoteId") Long evolutionNoteId){
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}",
				institutionId, internmentEpisodeId, evolutionNoteId);
		EvolutionNoteBo evolutionNoteBo = evolutionNoteService.getDocument(evolutionNoteId);
		ResponseEvolutionNoteDto result = evolutionNoteMapper.fromEvolutionNote(evolutionNoteBo);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}


	@DeleteMapping("/{evolutionNoteId}")
	public ResponseEntity<Boolean> deleteEvolutionNote(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "evolutionNoteId")  Long evolutionNoteId,
			@RequestBody String reason) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, " +
						"evolutionNoteId {}, reason {}",
				institutionId, internmentEpisodeId, evolutionNoteId, reason);
		deleteEvolutionNoteService.execute(internmentEpisodeId, evolutionNoteId, reason);
		LOG.debug(OUTPUT, Boolean.TRUE);
		return  ResponseEntity.ok().body(Boolean.TRUE);
	}

	@PutMapping("/{evolutionNoteId}")
	public ResponseEntity<Long> updateAnamnesis(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "evolutionNoteId") Long evolutionNoteId,
			@RequestBody @Valid EvolutionNoteDto evolutionNoteDto) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, evolutionNoteId {}, evolutionNoteDto {}",
				institutionId, internmentEpisodeId, evolutionNoteId, evolutionNoteDto);
		EvolutionNoteBo newEvolution = evolutionNoteMapper.fromEvolutionNoteDto(evolutionNoteDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(newEvolution::setPatientInfo, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		newEvolution.setPatientId(newEvolution.getPatientInfo().getId());
		newEvolution.setInstitutionId(institutionId);
		newEvolution.setEncounterId(internmentEpisodeId);
		Long result = updateEvolutionNoteService.execute(internmentEpisodeId, evolutionNoteId, newEvolution);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

}