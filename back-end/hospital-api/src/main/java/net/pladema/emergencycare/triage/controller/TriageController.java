package net.pladema.emergencycare.triage.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.EVitalSign;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewVitalSignsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.VitalSignObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service.VitalSignExternalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.controller.mapper.TriageVitalSignMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;
import net.pladema.emergencycare.triage.controller.dto.TriageAdultGynecologicalDto;
import net.pladema.emergencycare.triage.controller.dto.TriageBreathingDto;
import net.pladema.emergencycare.triage.controller.dto.TriageCirculationDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.emergencycare.triage.controller.mapper.TriageMapper;
import net.pladema.emergencycare.triage.controller.mapper.TriageMasterDataMapper;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;
import net.pladema.medicalconsultation.doctorsoffice.controller.service.DoctorsOfficeExternalService;
import net.pladema.user.controller.dto.UserDto;
import net.pladema.user.controller.service.UserPersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/triage")
@Tag(name = "Triage", description = "Triage")
public class TriageController {

    private static final Logger LOG = LoggerFactory.getLogger(TriageController.class);

    private final TriageService triageService;

    private final TriageMapper triageMapper;

    private final TriageMasterDataService triageMasterDataService;

    private final TriageMasterDataMapper triageMasterDataMapper;

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final VitalSignExternalService vitalSignExternalService;

    private final TriageVitalSignMapper triageVitalSignMapper;

    private final DoctorsOfficeExternalService doctorsOfficeExternalService;

    private final UserPersonExternalService userPersonExternalService;

    private final EmergencyCareMapper emergencyCareMapper;

    public TriageController(TriageService triageService,
                            TriageMapper triageMapper,
                            TriageMasterDataService triageMasterDataService,
                            TriageMasterDataMapper triageMasterDataMapper,
                            EmergencyCareEpisodeService emergencyCareEpisodeService,
                            VitalSignExternalService vitalSignExternalService,
                            TriageVitalSignMapper triageVitalSignMapper, DoctorsOfficeExternalService doctorsOfficeExternalService,
                            UserPersonExternalService userPersonExternalService,
                            EmergencyCareMapper emergencyCareMapper){
        super();
        this.triageService=triageService;
        this.triageMapper=triageMapper;
        this.triageMasterDataService = triageMasterDataService;
        this.triageMasterDataMapper = triageMasterDataMapper;
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.vitalSignExternalService = vitalSignExternalService;
        this.triageVitalSignMapper = triageVitalSignMapper;
        this.doctorsOfficeExternalService = doctorsOfficeExternalService;
        this.userPersonExternalService = userPersonExternalService;
        this.emergencyCareMapper = emergencyCareMapper;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Collection<TriageListDto>> getAll(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name="episodeId") Integer episodeId) {
        LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
        List<TriageBo> triages = triageService.getAll(institutionId, episodeId);
        List<TriageListDto> result = new ArrayList<>();
        triages.forEach(triageBo -> {
            TriageListDto triageListDto = createTriageListDto(triageBo);
            result.add(triageListDto);
        });
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    private TriageListDto createTriageListDto(TriageBo triageBo) {
        LOG.debug("Input parameter -> triageBo {}", triageBo);
        TriageListDto result = triageMapper.toTriageListDto(triageBo);
        // set user data
        UserDto userDto = userPersonExternalService.getUser(triageBo.getCreatedBy()).get();
        result.setCreatedBy(emergencyCareMapper.toEmergencyCareUserDto(userDto));
        // set doctor's office data
        if (triageBo.getDoctorsOfficeId() != null)
            result.setDoctorsOffice(doctorsOfficeExternalService.getDoctorsOfficeById(triageBo.getDoctorsOfficeId()));
        // set triage category
        TriageCategoryBo category = triageMasterDataService.getCategoryById(triageBo.getCategoryId());
        result.setCategory(triageMasterDataMapper.toTriageCategoryDto(category));
        // set vital signs data
        triageBo.getVitalSignIds().forEach(vitalSign -> {
            VitalSignObservationDto vitalSignObservationDto = vitalSignExternalService.getVitalSignObservationById(vitalSign);
            if (triageBo.isAdultGynecological())
                setVitalSignAsAdultGynecological(result, vitalSignObservationDto);
            else if (triageBo.isPediatric())
                setVitalSignAsPediatric(result, vitalSignObservationDto);
        });
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void setVitalSignAsAdultGynecological(TriageListDto triageListDto, VitalSignObservationDto vitalSignObservationDto) {
        LOG.debug("Input parameters -> triageListDto {}, vitalSignObservationDto {}", triageListDto, vitalSignObservationDto);
        if (triageListDto.getVitalSigns() == null)
            triageListDto.setVitalSigns(new NewVitalSignsObservationDto());

        if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.BLOOD_OXYGEN_SATURATION.getLoincCode())) {
            triageListDto.getVitalSigns().setBloodOxygenSaturation(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.HEART_RATE.getLoincCode())) {
            triageListDto.getVitalSigns().setHeartRate(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.RESPIRATORY_RATE.getLoincCode())) {
            triageListDto.getVitalSigns().setRespiratoryRate(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.TEMPERATURE.getLoincCode())) {
            triageListDto.getVitalSigns().setTemperature(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.SYSTOLIC_BLOOD_PRESSURE.getLoincCode())) {
            triageListDto.getVitalSigns().setSystolicBloodPressure(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.DIASTOLIC_BLOOD_PRESSURE.getLoincCode())) {
            triageListDto.getVitalSigns().setDiastolicBloodPressure(vitalSignObservationDto.getVitalSignObservation());

        }
    }

    private void setVitalSignAsPediatric(TriageListDto triageListDto, VitalSignObservationDto vitalSignObservationDto) {
        LOG.debug("Input parameters -> triageListDto {}, vitalSignObservationDto {}", triageListDto, vitalSignObservationDto);
        if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.BLOOD_OXYGEN_SATURATION.getLoincCode())) {
            if (triageListDto.getBreathing() == null)
                triageListDto.setBreathing(new TriageBreathingDto());
            triageListDto.getBreathing().setBloodOxygenSaturation(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.HEART_RATE.getLoincCode())) {
            if (triageListDto.getCirculation() == null)
                triageListDto.setCirculation(new TriageCirculationDto());
            triageListDto.getCirculation().setHeartRate(vitalSignObservationDto.getVitalSignObservation());

        } else if (vitalSignObservationDto.getLoincCode().equals(EVitalSign.RESPIRATORY_RATE.getLoincCode())) {
            if (triageListDto.getBreathing() == null)
                triageListDto.setBreathing(new TriageBreathingDto());
            triageListDto.getBreathing().setRespiratoryRate(vitalSignObservationDto.getVitalSignObservation());

        }
    }

    @Transactional
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdministrative(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("episodeId") Integer episodeId,
            @RequestBody TriageAdministrativeDto body) {
        LOG.debug("Add triage administrative => {}", body);
        TriageBo triage = triageMapper.toTriageBo(body);
        triage.setEmergencyCareEpisodeId(episodeId);
        triage = triageService.createAdministrative(triage, institutionId);
        Integer result = triage.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> newAdultGynecological(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("episodeId") Integer episodeId,
            @RequestBody TriageAdultGynecologicalDto body){
        LOG.debug("Add triage adult-gynecological => {}", body);
        TriageBo triage = triageMapper.toTriageBo(body);
        triage.setEmergencyCareEpisodeId(episodeId);
        Integer patientId = emergencyCareEpisodeService.get(episodeId, institutionId).getPatient() != null ? emergencyCareEpisodeService.get(episodeId, institutionId).getPatient().getId() : null;
        NewVitalSignsObservationDto vitalSignsObservationDto = vitalSignExternalService.saveVitalSigns(patientId, body.getVitalSigns());
        triage.setVitalSignIds(getVitalSignIds(vitalSignsObservationDto));
        triage = triageService.createAdultGynecological(triage, institutionId);
        Integer result = triage.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/pediatric")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> newPediatric(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("episodeId") Integer episodeId,
            @RequestBody TriagePediatricDto body){
        LOG.debug("Add triage pediatric => {}", body);
        TriageBo triage = triageMapper.toTriageBo(body);
        triage.setEmergencyCareEpisodeId(episodeId);
        Integer patientId = emergencyCareEpisodeService.get(episodeId, institutionId).getPatient() != null ? emergencyCareEpisodeService.get(episodeId, institutionId).getPatient().getId() : null;
        NewVitalSignsObservationDto vitalSignsObservationDto = triageVitalSignMapper.fromTriagePediatricDto(body);
        vitalSignsObservationDto = vitalSignExternalService.saveVitalSigns(patientId, vitalSignsObservationDto);
        triage.setVitalSignIds(getVitalSignIds(vitalSignsObservationDto));
        triage = triageService.createPediatric(triage, institutionId);
        Integer result = triage.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
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
