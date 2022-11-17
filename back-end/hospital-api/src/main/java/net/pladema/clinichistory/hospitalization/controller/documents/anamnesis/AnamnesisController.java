package net.pladema.clinichistory.hospitalization.controller.documents.anamnesis;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.AnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.dto.ResponseAnamnesisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.anamnesis.mapper.AnamnesisMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.DeleteAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.UpdateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anamnesis")
@Tag(name = "Anamnesis", description = "Anamnesis")
@Validated
public class AnamnesisController {

    private static final Logger LOG = LoggerFactory.getLogger(AnamnesisController.class);
    public static final String OUTPUT = "Output -> {}";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateAnamnesisService createAnamnesisService;

    private final AnamnesisService anamnesisService;

    private final AnamnesisMapper anamnesisMapper;

    private final MessageSource messageSource;

    private final PatientExternalService patientExternalService;

    private final DeleteAnamnesisService deleteAnamnesisService;

	private final UpdateAnamnesisService updateAnamnesisService;

    public AnamnesisController(InternmentEpisodeService internmentEpisodeService,
							   CreateAnamnesisService createAnamnesisService,
							   AnamnesisService anamnesisService,
							   AnamnesisMapper anamnesisMapper,
							   MessageSource messageSource,
							   PatientExternalService patientExternalService,
							   DeleteAnamnesisService deleteAnamnesisService,
							   UpdateAnamnesisService updateAnamnesisService) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createAnamnesisService = createAnamnesisService;
        this.anamnesisService = anamnesisService;
        this.anamnesisMapper = anamnesisMapper;
        this.messageSource = messageSource;
        this.patientExternalService = patientExternalService;
        this.deleteAnamnesisService = deleteAnamnesisService;
		this.updateAnamnesisService = updateAnamnesisService;
    }

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<Boolean> createAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody AnamnesisDto anamnesisDto) {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, anamnesisDto);
        AnamnesisBo anamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
        anamnesis.setEncounterId(internmentEpisodeId);
        anamnesis.setInstitutionId(institutionId);

        internmentEpisodeService.getPatient(internmentEpisodeId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(anamnesis::setPatientInfo, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));

        createAnamnesisService.execute(anamnesis);

        LOG.debug(OUTPUT, Boolean.TRUE);
        return  ResponseEntity.ok().body(Boolean.TRUE);
    }


    @GetMapping("/{anamnesisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.ANAMNESIS)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
    public ResponseEntity<ResponseAnamnesisDto> getAnamnesis(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "anamnesisId") Long anamnesisId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}",
                institutionId, internmentEpisodeId, anamnesisId);
        AnamnesisBo anamnesis = anamnesisService.getDocument(anamnesisId);
        ResponseAnamnesisDto result = anamnesisMapper.fromAnamnesis(anamnesis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

	@DeleteMapping("/{anamnesisId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
	public ResponseEntity<Boolean> deleteAnamnesis(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "anamnesisId") Long anamnesisId,
			@RequestBody String reason) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, " +
						"anamnesisId {}, reason {}",
				institutionId, internmentEpisodeId, anamnesisId, reason);
		deleteAnamnesisService.execute(internmentEpisodeId, anamnesisId, reason);
		LOG.debug(OUTPUT, Boolean.TRUE);
		return  ResponseEntity.ok().body(Boolean.TRUE);
	}

	@PutMapping("/{anamnesisId}")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
	public ResponseEntity<Long> updateAnamnesis(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
			@PathVariable(name = "anamnesisId") Long anamnesisId,
			@RequestBody AnamnesisDto anamnesisDto) {
		LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, anamnesisId {}, newAnanmnesis {}",
				institutionId, internmentEpisodeId, anamnesisId, anamnesisDto);
		AnamnesisBo newAnamnesis = anamnesisMapper.fromAnamnesisDto(anamnesisDto);
		internmentEpisodeService.getPatient(internmentEpisodeId)
				.map(patientExternalService::getBasicDataFromPatient)
				.map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.ifPresentOrElse(newAnamnesis::setPatientInfo, () -> new NotFoundException("El paciente no existe", "El paciente no existe"));
		newAnamnesis.setPatientId(newAnamnesis.getPatientInfo().getId());
		newAnamnesis.setInstitutionId(institutionId);
		newAnamnesis.setEncounterId(internmentEpisodeId);
		Long result = updateAnamnesisService.execute(internmentEpisodeId, anamnesisId, newAnamnesis);
		LOG.debug(OUTPUT, result);
		return  ResponseEntity.ok().body(result);
	}

}