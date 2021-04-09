package net.pladema.emergencycare.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.controller.dto.NewVitalSignsObservationDto;
import net.pladema.clinichistory.documents.controller.service.VitalSignExternalService;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.SnomedMapper;
import net.pladema.clinichistory.hospitalization.controller.generalstate.mapper.VitalSignMapper;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.service.ReasonExternalService;
import net.pladema.emergencycare.controller.dto.ECAdministrativeDto;
import net.pladema.emergencycare.controller.dto.ECAdultGynecologicalDto;
import net.pladema.emergencycare.controller.dto.ECPediatricDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.dates.controller.dto.DateTimeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes")
@Api(value = "Emergency care Episodes", tags = {"Emergency care Episodes"})
@Validated
public class EmergencyCareEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeController.class);

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final EmergencyCareMapper emergencyCareMapper;

    private final ReasonExternalService reasonExternalService;

    private final VitalSignExternalService vitalSignExternalService;

    private final SnomedMapper snomedMapper;

    private final VitalSignMapper vitalSignMapper;

    private final LocalDateMapper localDateMapper;

    public EmergencyCareEpisodeController(EmergencyCareEpisodeService emergencyCareEpisodeService,
                                          EmergencyCareMapper emergencyCareMapper,
                                          ReasonExternalService reasonExternalService,
                                          VitalSignExternalService vitalSignExternalService,
                                          SnomedMapper snomedMapper,
                                          VitalSignMapper vitalSignMapper, LocalDateMapper localDateMapper){
        super();
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.emergencyCareMapper=emergencyCareMapper;
        this.reasonExternalService = reasonExternalService;
        this.vitalSignExternalService = vitalSignExternalService;
        this.snomedMapper = snomedMapper;
        this.vitalSignMapper = vitalSignMapper;
        this.localDateMapper = localDateMapper;
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
            @Valid @RequestBody ECAdministrativeDto body) {
        LOG.debug("Add emergency care administrative episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.administrativeEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);
        List<SnomedDto> reasons = reasonExternalService.addReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));
        newEmergencyCare = emergencyCareEpisodeService.createAdministrative(newEmergencyCare, institutionId);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PutMapping("/{emergencyCareEpisodeId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> updateAdministrative(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "emergencyCareEpisodeId") Integer emergencyCareEpisodeId,
            @Valid @RequestBody ECAdministrativeDto body) {
        LOG.debug("Update emergency care administrative episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.administrativeEmergencyCareDtoToEmergencyCareBo(body);
        List<SnomedDto> reasons = reasonExternalService.addReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));
        newEmergencyCare.setInstitutionId(institutionId);
        newEmergencyCare.setId(emergencyCareEpisodeId);
        Integer result = emergencyCareEpisodeService.updateAdministrative(newEmergencyCare, institutionId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdult(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody ECAdultGynecologicalDto body) {
        LOG.debug("Add emergency care adult-gynecological episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.adultGynecologicalEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);

        NewVitalSignsObservationDto vitalSignsObservationDto =
                vitalSignExternalService.saveVitalSigns(body.patientId(), body.vitalSignsObservation());
        newEmergencyCare.setTriageVitalSignIds(getVitalSignIds(vitalSignsObservationDto));

        List<SnomedDto> reasons = reasonExternalService.addReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));

        newEmergencyCare = emergencyCareEpisodeService.createAdult(newEmergencyCare, institutionId);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/pediatric")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createPediatric(@PathVariable(name = "institutionId") Integer institutionId,
                                                   @RequestBody ECPediatricDto body) {
        LOG.debug("Add emergency care pediatric episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.pediatricEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);

        NewVitalSignsObservationDto vitalSignsObservationDto = vitalSignMapper.fromTriagePediatricDto(body.getTriage());
        vitalSignsObservationDto = vitalSignExternalService.saveVitalSigns(body.patientId(), vitalSignsObservationDto);
        newEmergencyCare.setTriageVitalSignIds(getVitalSignIds(vitalSignsObservationDto));

        List<SnomedDto> reasons = reasonExternalService.addReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));

        newEmergencyCare = emergencyCareEpisodeService.createPediatric(newEmergencyCare, institutionId);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{episodeId}/creation-date")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<DateTimeDto> getCreationDate(@PathVariable(name = "institutionId") Integer institutionId,
                                                       @PathVariable(name = "episodeId") Integer episodeId) {
        LOG.debug("Get episode creation date -> institutionId {}, episodeId {}", institutionId, episodeId);
        EmergencyCareBo emergencyCareBo = emergencyCareEpisodeService.get(episodeId,institutionId);
        LocalDateTime creationDate = emergencyCareBo.getCreatedOn();
        DateTimeDto output = localDateMapper.toDateTimeDto(creationDate);
        LOG.debug("Output -> {}", output);
        return ResponseEntity.ok().body(output);
    }

    private List<Integer> getVitalSignIds(NewVitalSignsObservationDto vitalSignsObservationDto){
        LOG.debug("Input parameter -> vitalSignsObservationDto {}", vitalSignsObservationDto);
        List<Integer> result = new ArrayList<>();
        if (vitalSignsObservationDto.getSystolicBloodPressure() != null && vitalSignsObservationDto.getSystolicBloodPressure().getId() != null)
            result.add(vitalSignsObservationDto.getSystolicBloodPressure().getId());
        if (vitalSignsObservationDto.getDiastolicBloodPressure() != null && vitalSignsObservationDto.getDiastolicBloodPressure().getId() != null)
            result.add(vitalSignsObservationDto.getDiastolicBloodPressure().getId());
        if (vitalSignsObservationDto.getTemperature() != null && vitalSignsObservationDto.getTemperature().getId() != null)
            result.add(vitalSignsObservationDto.getTemperature().getId());
        if (vitalSignsObservationDto.getHeartRate() != null && vitalSignsObservationDto.getHeartRate().getId() != null)
            result.add(vitalSignsObservationDto.getHeartRate().getId());
        if (vitalSignsObservationDto.getRespiratoryRate() != null && vitalSignsObservationDto.getRespiratoryRate().getId() != null)
            result.add(vitalSignsObservationDto.getRespiratoryRate().getId());
        if (vitalSignsObservationDto.getBloodOxygenSaturation() != null && vitalSignsObservationDto.getBloodOxygenSaturation().getId() != null)
            result.add(vitalSignsObservationDto.getBloodOxygenSaturation().getId());
        LOG.debug("Output -> {}", result);
        return result;
    }
}
