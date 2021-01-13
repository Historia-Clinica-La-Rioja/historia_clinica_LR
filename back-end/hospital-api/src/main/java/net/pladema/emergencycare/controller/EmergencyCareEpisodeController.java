package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.service.ReasonExternalService;
import net.pladema.emergencycare.controller.dto.ECAdministrativeDto;
import net.pladema.emergencycare.controller.dto.ECAdultGynecologicalDto;
import net.pladema.emergencycare.controller.dto.ECPediatricDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.ResponseEmergencyCareDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes")
@Api(value = "Emergency care Episodes", tags = {"Emergency care Episodes"})
public class EmergencyCareEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeController.class);

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final EmergencyCareMapper emergencyCareMapper;

    private final ReasonExternalService reasonExternalService;

    private final PersonExternalService personExternalService;

    private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

    public EmergencyCareEpisodeController(EmergencyCareEpisodeService emergencyCareEpisodeService,
                                          EmergencyCareMapper emergencyCareMapper,
                                          ReasonExternalService reasonExternalService, PersonExternalService personExternalService, PatientExternalMedicalCoverageService patientExternalMedicalCoverageService){
        super();
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.emergencyCareMapper=emergencyCareMapper;
        this.reasonExternalService = reasonExternalService;
        this.personExternalService = personExternalService;
        this.patientExternalMedicalCoverageService = patientExternalMedicalCoverageService;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Collection<EmergencyCareListDto>> getAll(
            @PathVariable(name = "institutionId") Integer institutionId) {
        LOG.debug("Input parameters -> institutionId {}", institutionId);
        List<EmergencyCareBo> episodes = emergencyCareEpisodeService.getAll(institutionId);
        List<EmergencyCareListDto> result = emergencyCareMapper.toListEmergencyCareListDto(episodes);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdministrative(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody ECAdministrativeDto body) {
        LOG.debug("Add emergency care administrative episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.administrativeEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);
        List<String> reasonIds = (body.getAdministrative() != null && body.getAdministrative().getReasons() != null) ?
                    reasonExternalService.addReasons(body.getAdministrative().getReasons()) : new ArrayList<>();
        newEmergencyCare.setReasonIds(reasonIds);
        newEmergencyCare = emergencyCareEpisodeService.createAdministrative(newEmergencyCare);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdult(@RequestBody ECAdultGynecologicalDto body) {
        LOG.debug("Add emergency care adult-gynecological episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.adultGynecologicalEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare = emergencyCareEpisodeService.createAdult(newEmergencyCare);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/pediatric")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createPediatric(@RequestBody ECPediatricDto body) {
        LOG.debug("Add emergency care pediatric episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.pediatricEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare = emergencyCareEpisodeService.createPediatric(newEmergencyCare);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{episodeId}/administrative")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
	public ResponseEntity<ResponseEmergencyCareDto> getAdministrative(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		EmergencyCareBo episode = emergencyCareEpisodeService.get(episodeId, institutionId);
		ResponseEmergencyCareDto result = emergencyCareMapper.toResponseEmergencyCareDto(episode);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

    @GetMapping("/{episodeId}/state")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<MasterDataProjection> getState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId) {
        LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
        MasterDataProjection result = emergencyCareEpisodeService.getState(episodeId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{episodeId}/state/change")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Boolean> changeState(
            @PathVariable(name = "episodeId") Integer episodeId,
            @RequestParam(name = "emergencyCareStateId") Short emergencyCareStateId,
            @RequestParam(name = "doctorsOfficeId", defaultValue = "-1") Integer doctorsOfficeId) {
        LOG.debug("Change emergency care state -> episodeId {}, emergencyCareStateId {}, doctorsOfficeId {}",
                episodeId, emergencyCareStateId, doctorsOfficeId);
        Boolean result = emergencyCareEpisodeService.changeState(episodeId, emergencyCareStateId, doctorsOfficeId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PutMapping("/{episodeId}/administrative/patient/{patientId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Boolean> setPatient(
            @PathVariable(name = "episodeId") Integer episodeId,
            @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug("Update patient of emergency care administrative episode -> episodeId {}, patientId {}",
                episodeId, patientId);
        Boolean result = emergencyCareEpisodeService.setPatient(episodeId, patientId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
