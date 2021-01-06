package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.service.ReasonExternalService;
import net.pladema.emergencycare.controller.dto.AdministrativeEmergencyCareDto;
import net.pladema.emergencycare.controller.dto.AdultGynecologicalEmergencyCareDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.PediatricEmergencyCareDto;
import net.pladema.emergencycare.controller.dto.administrative.ResponseEmergencyCareDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes")
@Api(value = "Emergency care Episodes", tags = { "Emergency care Episodes" })
public class EmergencyCareEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeController.class);

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final EmergencyCareMapper emergencyCareMapper;

    private final ReasonExternalService reasonExternalService;

    public EmergencyCareEpisodeController(EmergencyCareEpisodeService emergencyCareEpisodeService,
                                          EmergencyCareMapper emergencyCareMapper,
                                          ReasonExternalService reasonExternalService){
        super();
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.emergencyCareMapper=emergencyCareMapper;
        this.reasonExternalService = reasonExternalService;
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
            @RequestBody AdministrativeEmergencyCareDto body) {
        LOG.debug("Add emergency care administrative episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.administrativeEmergencyCareDtoToEmergencyCareBo(body);
        setEpisodeAttributes(body, newEmergencyCare);
        List<String> reasonIds = (body.getAdministrative() != null && body.getAdministrative().getReasons() != null) ?
                    reasonExternalService.addReasons(body.getAdministrative().getReasons()) : new ArrayList<>();
        newEmergencyCare.setReasonIds(reasonIds);
        newEmergencyCare = emergencyCareEpisodeService.createAdministrative(newEmergencyCare);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    private void setEpisodeAttributes(AdministrativeEmergencyCareDto body, EmergencyCareBo newEmergencyCare) {
        LOG.debug("Input parameters -> body {}, newEmergencyCare {}", body, newEmergencyCare);
        if (body.getAdministrative() == null)
            return;
        if (body.getAdministrative().getTypeId() != null)
            newEmergencyCare.setEmergencyCareTypeById(body.getAdministrative().getTypeId());
        if (body.getAdministrative().getEntranceTypeId() != null)
            newEmergencyCare.setEmergencyEntranceById(body.getAdministrative().getEntranceTypeId());
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdult(@RequestBody AdultGynecologicalEmergencyCareDto body) {
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
    public ResponseEntity<Integer> createPediatric(@RequestBody PediatricEmergencyCareDto body) {
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
        EmergencyCareBo episode = emergencyCareEpisodeService.get(episodeId);
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
