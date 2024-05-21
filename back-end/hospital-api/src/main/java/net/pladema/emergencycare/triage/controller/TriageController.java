package net.pladema.emergencycare.triage.controller;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service.RiskFactorExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.controller.mapper.TriageRiskFactorMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.triage.controller.dto.TriageAdministrativeDto;
import net.pladema.emergencycare.triage.infrastructure.input.rest.dto.TriageAdultGynecologicalDto;
import net.pladema.emergencycare.triage.controller.dto.TriageListDto;
import net.pladema.emergencycare.triage.controller.dto.TriagePediatricDto;
import net.pladema.emergencycare.triage.controller.mapper.TriageListMapper;
import net.pladema.emergencycare.triage.infrastructure.input.rest.mapper.TriageMapper;
import net.pladema.emergencycare.triage.controller.mapper.TriageMasterDataMapper;
import net.pladema.emergencycare.triage.service.TriageMasterDataService;
import net.pladema.emergencycare.triage.service.TriageService;
import net.pladema.emergencycare.triage.domain.TriageBo;
import net.pladema.medicalconsultation.doctorsoffice.controller.service.DoctorsOfficeExternalService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.user.controller.service.UserPersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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

	private final PatientExternalService patientExternalService;

    private final EmergencyCareMapper emergencyCareMapper;

	private final RiskFactorMapper riskFactorMapper;

	private final TriageListMapper triageListMapper;

    public TriageController(TriageService triageService,
							TriageMapper triageMapper,
							TriageMasterDataService triageMasterDataService,
							TriageMasterDataMapper triageMasterDataMapper,
							EmergencyCareEpisodeService emergencyCareEpisodeService,
							RiskFactorExternalService riskFactorExternalService,
							TriageRiskFactorMapper triageRiskFactorMapper, DoctorsOfficeExternalService doctorsOfficeExternalService,
							UserPersonExternalService userPersonExternalService,
							PatientExternalService patientExternalService,
							EmergencyCareMapper emergencyCareMapper,
							RiskFactorMapper riskFactorMapper,
							TriageListMapper triageListMapper){
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
		this.patientExternalService = patientExternalService;
        this.emergencyCareMapper = emergencyCareMapper;
		this.riskFactorMapper = riskFactorMapper;
		this.triageListMapper = triageListMapper;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Collection<TriageListDto>> getAll(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name="episodeId") Integer episodeId) {
        LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
        List<TriageBo> triages = triageService.getAll(institutionId, episodeId);
        List<TriageListDto> result = new ArrayList<>();
        triages.forEach(triageBo -> {
            TriageListDto triageListDto = triageListMapper.toTriageListDto(triageBo);
            result.add(triageListDto);
        });
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> createAdministrative(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("episodeId") Integer episodeId,
            @RequestBody TriageAdministrativeDto body) {
        LOG.debug("Add triage administrative => {}", body);
		assertValidTriageLevel(body.getCategoryId());
        TriageBo triage = triageMapper.toTriageBo(body);
        triage.setEmergencyCareEpisodeId(episodeId);
		Integer patientId = emergencyCareEpisodeService.get(episodeId, institutionId).getPatient() != null ? emergencyCareEpisodeService.get(episodeId, institutionId).getPatient().getId() : null;
		triage.setPatientInfo(new PatientInfoBo(patientId));
		triage = triageService.createAdministrative(triage, institutionId);
        Integer result = triage.getTriageId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> newAdultGynecological(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("episodeId") Integer episodeId,
            @RequestBody TriageAdultGynecologicalDto body){
        LOG.debug("Add triage adult-gynecological => {}", body);
		assertValidTriageLevel(body.getCategoryId());
        TriageBo triage = triageMapper.toTriageBo(body);
        triage.setEmergencyCareEpisodeId(episodeId);
        Integer patientId = emergencyCareEpisodeService.get(episodeId, institutionId).getPatient() != null ? emergencyCareEpisodeService.get(episodeId, institutionId).getPatient().getId() : null;
        NewRiskFactorsObservationDto riskFactorsObservationDto = riskFactorExternalService.saveRiskFactors(patientId, body.getRiskFactors());
        triage.setRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));
		setRiskFactorsAndPatientInfo(triage, patientId, riskFactorsObservationDto);
        triage = triageService.createAdultGynecological(triage, institutionId);
        Integer result = triage.getTriageId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/pediatric")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> newPediatric(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("episodeId") Integer episodeId,
            @RequestBody TriagePediatricDto body){
        LOG.debug("Add triage pediatric => {}", body);
		assertValidTriageLevel(body.getCategoryId());
        TriageBo triage = triageMapper.toTriageBo(body);
        triage.setEmergencyCareEpisodeId(episodeId);
        Integer patientId = emergencyCareEpisodeService.get(episodeId, institutionId).getPatient() != null ? emergencyCareEpisodeService.get(episodeId, institutionId).getPatient().getId() : null;
        NewRiskFactorsObservationDto riskFactorsObservationDto = triageRiskFactorMapper.fromTriagePediatricDto(body);
        riskFactorsObservationDto = riskFactorExternalService.saveRiskFactors(patientId, riskFactorsObservationDto);
        triage.setRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));
		setRiskFactorsAndPatientInfo(triage, patientId, riskFactorsObservationDto);
        triage = triageService.createPediatric(triage, institutionId);
        Integer result = triage.getTriageId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

	private void assertValidTriageLevel(Short categoryId) {
		final Short WITHOUT_TRIAGE_LEVEL_ID = 6;
		Assert.isTrue(!categoryId.equals(WITHOUT_TRIAGE_LEVEL_ID), "El nivel de triage no puede ser \"Sin triage\" ");
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

	private void setRiskFactorsAndPatientInfo(TriageBo triage, Integer patientId, NewRiskFactorsObservationDto riskFactorsObservationDto) {
		RiskFactorBo riskFactors = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
		if (!riskFactors.isEmpty())
			riskFactorExternalService.formatRiskFactors(riskFactors);
		triage.setRiskFactors(riskFactors);
		BasicPatientDto patient = patientExternalService.getBasicDataFromPatient(patientId);
		if (patient.getPerson() != null)
			triage.setPatientInfo(new PatientInfoBo(patient.getId(), patient.getPerson().getGender().getId(), patient.getPerson().getAge()));
		else
			triage.setPatientInfo(new PatientInfoBo(patient.getId()));
	}

}
