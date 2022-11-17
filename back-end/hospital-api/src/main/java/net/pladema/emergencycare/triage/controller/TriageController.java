package net.pladema.emergencycare.triage.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.ERiskFactor;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service.RiskFactorExternalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.controller.mapper.TriageRiskFactorMapper;
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

    private final RiskFactorExternalService riskFactorExternalService;

    private final TriageRiskFactorMapper triageRiskFactorMapper;

    private final DoctorsOfficeExternalService doctorsOfficeExternalService;

    private final UserPersonExternalService userPersonExternalService;

    private final EmergencyCareMapper emergencyCareMapper;

    public TriageController(TriageService triageService,
							TriageMapper triageMapper,
							TriageMasterDataService triageMasterDataService,
							TriageMasterDataMapper triageMasterDataMapper,
							EmergencyCareEpisodeService emergencyCareEpisodeService,
							RiskFactorExternalService riskFactorExternalService,
							TriageRiskFactorMapper triageRiskFactorMapper, DoctorsOfficeExternalService doctorsOfficeExternalService,
							UserPersonExternalService userPersonExternalService,
							EmergencyCareMapper emergencyCareMapper){
        super();
        this.triageService=triageService;
        this.triageMapper=triageMapper;
        this.triageMasterDataService = triageMasterDataService;
        this.triageMasterDataMapper = triageMasterDataMapper;
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.riskFactorExternalService = riskFactorExternalService;
        this.triageRiskFactorMapper = triageRiskFactorMapper;
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
        // set risk factors data
        triageBo.getRiskFactorIds().forEach(riskFactor -> {
            RiskFactorObservationDto riskFactorObservationDto = riskFactorExternalService.getRiskFactorObservationById(riskFactor);
            if (triageBo.isAdultGynecological())
                setRiskFactorAsAdultGynecological(result, riskFactorObservationDto);
            else if (triageBo.isPediatric())
                setRiskFactorAsPediatric(result, riskFactorObservationDto);
        });
        LOG.debug("Output -> {}", result);
        return result;
    }

    private void setRiskFactorAsAdultGynecological(TriageListDto triageListDto, RiskFactorObservationDto riskFactorObservationDto) {
        LOG.debug("Input parameters -> triageListDto {}, riskFactorObservationDto {}", triageListDto, riskFactorObservationDto);
        if (triageListDto.getRiskFactors() == null)
            triageListDto.setRiskFactors(new NewRiskFactorsObservationDto());

        if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.BLOOD_OXYGEN_SATURATION.getLoincCode())) {
            triageListDto.getRiskFactors().setBloodOxygenSaturation(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.HEART_RATE.getLoincCode())) {
            triageListDto.getRiskFactors().setHeartRate(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.RESPIRATORY_RATE.getLoincCode())) {
            triageListDto.getRiskFactors().setRespiratoryRate(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.TEMPERATURE.getLoincCode())) {
            triageListDto.getRiskFactors().setTemperature(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.SYSTOLIC_BLOOD_PRESSURE.getLoincCode())) {
            triageListDto.getRiskFactors().setSystolicBloodPressure(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.DIASTOLIC_BLOOD_PRESSURE.getLoincCode())) {
            triageListDto.getRiskFactors().setDiastolicBloodPressure(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.BLOOD_GLUCOSE.getLoincCode())){
        	triageListDto.getRiskFactors().setBloodGlucose(riskFactorObservationDto.getRiskFactorObservation());

		} else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.GLYCOSYLATED_HEMOGLOBIN.getLoincCode())){
			triageListDto.getRiskFactors().setGlycosylatedHemoglobin(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.CARDIOVASCULAR_RISK.getLoincCode())) {
			triageListDto.getRiskFactors().setCardiovascularRisk(riskFactorObservationDto.getRiskFactorObservation());
		}

    }

    private void setRiskFactorAsPediatric(TriageListDto triageListDto, RiskFactorObservationDto riskFactorObservationDto) {
        LOG.debug("Input parameters -> triageListDto {}, riskFactorObservationDto {}", triageListDto, riskFactorObservationDto);
        if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.BLOOD_OXYGEN_SATURATION.getLoincCode())) {
            if (triageListDto.getBreathing() == null)
                triageListDto.setBreathing(new TriageBreathingDto());
            triageListDto.getBreathing().setBloodOxygenSaturation(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.HEART_RATE.getLoincCode())) {
            if (triageListDto.getCirculation() == null)
                triageListDto.setCirculation(new TriageCirculationDto());
            triageListDto.getCirculation().setHeartRate(riskFactorObservationDto.getRiskFactorObservation());

        } else if (riskFactorObservationDto.getLoincCode().equals(ERiskFactor.RESPIRATORY_RATE.getLoincCode())) {
            if (triageListDto.getBreathing() == null)
                triageListDto.setBreathing(new TriageBreathingDto());
            triageListDto.getBreathing().setRespiratoryRate(riskFactorObservationDto.getRiskFactorObservation());

        }
    }


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
        NewRiskFactorsObservationDto riskFactorsObservationDto = riskFactorExternalService.saveRiskFactors(patientId, body.getRiskFactors());
        triage.setRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));
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
        NewRiskFactorsObservationDto riskFactorsObservationDto = triageRiskFactorMapper.fromTriagePediatricDto(body);
        riskFactorsObservationDto = riskFactorExternalService.saveRiskFactors(patientId, riskFactorsObservationDto);
        triage.setRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));
        triage = triageService.createPediatric(triage, institutionId);
        Integer result = triage.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    private List<Integer> getRiskFactorIds(NewRiskFactorsObservationDto riskFactorsObservationDto){
        LOG.debug("Input parameter -> riskFactorsObservationDto {}", riskFactorsObservationDto);
        List<Integer> result = new ArrayList<>();
        if (riskFactorsObservationDto.getSystolicBloodPressure() != null && riskFactorsObservationDto.getSystolicBloodPressure().getId() != null)
            result.add(riskFactorsObservationDto.getSystolicBloodPressure().getId());
        if (riskFactorsObservationDto.getDiastolicBloodPressure() != null && riskFactorsObservationDto.getDiastolicBloodPressure().getId() != null)
            result.add(riskFactorsObservationDto.getDiastolicBloodPressure().getId());
        if (riskFactorsObservationDto.getTemperature() != null && riskFactorsObservationDto.getTemperature().getId() != null)
            result.add(riskFactorsObservationDto.getTemperature().getId());
        if (riskFactorsObservationDto.getHeartRate() != null && riskFactorsObservationDto.getHeartRate().getId() != null)
            result.add(riskFactorsObservationDto.getHeartRate().getId());
        if (riskFactorsObservationDto.getRespiratoryRate() != null && riskFactorsObservationDto.getRespiratoryRate().getId() != null)
            result.add(riskFactorsObservationDto.getRespiratoryRate().getId());
        if (riskFactorsObservationDto.getBloodOxygenSaturation() != null && riskFactorsObservationDto.getBloodOxygenSaturation().getId() != null)
            result.add(riskFactorsObservationDto.getBloodOxygenSaturation().getId());
        if (riskFactorsObservationDto.getBloodGlucose() != null && riskFactorsObservationDto.getBloodGlucose().getId() != null)
        	result.add((riskFactorsObservationDto.getBloodGlucose().getId()));
		if (riskFactorsObservationDto.getGlycosylatedHemoglobin() != null && riskFactorsObservationDto.getGlycosylatedHemoglobin().getId() != null)
			result.add((riskFactorsObservationDto.getGlycosylatedHemoglobin().getId()));
		if (riskFactorsObservationDto.getCardiovascularRisk() != null && riskFactorsObservationDto.getCardiovascularRisk().getId() != null)
			result.add((riskFactorsObservationDto.getCardiovascularRisk().getId()));
        LOG.debug("Output -> {}", result);
        return result;
    }
}
