package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareDto;
import net.pladema.emergencycare.controller.dto.administrative.ResponseEmergencyCareDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Collection;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes")
@Api(value = "Emergency care Episodes", tags = { "Emergency care Episodes" })
public class EmergencyCareEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeController.class);

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final EmergencyCareMapper emergencyCareMapper;

    public EmergencyCareEpisodeController(EmergencyCareEpisodeService emergencyCareEpisodeService,
                                          EmergencyCareMapper emergencyCareMapper){
        super();
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.emergencyCareMapper=emergencyCareMapper;
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
    public ResponseEntity<Integer> createAdministrative(@RequestBody EmergencyCareDto body) {
        LOG.debug("Add emergency care administrative episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.toEmergencyCareBo(body);
        newEmergencyCare = emergencyCareEpisodeService.createAdministrative(newEmergencyCare);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdult(@RequestBody EmergencyCareDto body) {
        LOG.debug("Add emergency care adult-gynecological episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.toEmergencyCareBo(body);
        newEmergencyCare = emergencyCareEpisodeService.createAdult(newEmergencyCare);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/pediatric")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createPediatric(@RequestBody EmergencyCareDto body) {
        LOG.debug("Add emergency care pediatric episode => {}", body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.toEmergencyCareBo(body);
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
            @RequestParam(name = "doctorsOfficeId") Integer doctorsOfficeId) {
        LOG.debug("Change emergency care state -> episodeId {}, emergencyCareStateId {}, doctorsOfficeId {}",
                episodeId, emergencyCareStateId, doctorsOfficeId);
        Boolean result = emergencyCareEpisodeService.changeState(episodeId, emergencyCareStateId, doctorsOfficeId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
