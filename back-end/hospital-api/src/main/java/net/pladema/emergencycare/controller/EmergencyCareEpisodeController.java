package net.pladema.emergencycare.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.ReasonExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service.RiskFactorExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.controller.dto.ECAdministrativeDto;
import net.pladema.emergencycare.controller.dto.ECAdultGynecologicalDto;
import net.pladema.emergencycare.controller.dto.ECPediatricDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.NewEmergencyCareDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.controller.mapper.TriageRiskFactorMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import net.pladema.emergencycare.service.domain.PatientECEBo;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EPatientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes")
@Tag(name = "Emergency care episodes", description = "Emergency care episodes")
@Validated
public class EmergencyCareEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeController.class);

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final EmergencyCareMapper emergencyCareMapper;

    private final ReasonExternalService reasonExternalService;

    private final RiskFactorExternalService riskFactorExternalService;

	private final PatientService patientService;

    private final SnomedMapper snomedMapper;

    private final TriageRiskFactorMapper triageRiskFactorMapper;

    private final LocalDateMapper localDateMapper;

	private final RiskFactorMapper riskFactorMapper;

    public EmergencyCareEpisodeController(EmergencyCareEpisodeService emergencyCareEpisodeService,
										  EmergencyCareMapper emergencyCareMapper,
										  ReasonExternalService reasonExternalService,
										  RiskFactorExternalService riskFactorExternalService,
										  PatientService patientService,
										  SnomedMapper snomedMapper,
										  TriageRiskFactorMapper triageRiskFactorMapper, LocalDateMapper localDateMapper,
										  RiskFactorMapper riskFactorMapper){
        super();
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.emergencyCareMapper=emergencyCareMapper;
        this.reasonExternalService = reasonExternalService;
        this.riskFactorExternalService = riskFactorExternalService;
		this.patientService = patientService;
        this.snomedMapper = snomedMapper;
        this.triageRiskFactorMapper = triageRiskFactorMapper;
        this.localDateMapper = localDateMapper;
		this.riskFactorMapper = riskFactorMapper;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
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
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdministrative(
            @PathVariable(name = "institutionId") Integer institutionId,
            @Valid @RequestBody ECAdministrativeDto body) {
        LOG.debug("Add emergency care administrative episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.administrativeEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);
        List<SnomedDto> reasons = reasonExternalService.addSnomedReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));
		if (newEmergencyCare.getPatient() == null || newEmergencyCare.getPatient().getId() == null)
			newEmergencyCare.setPatient(createEmergencyCareEpisodePatient());
        newEmergencyCare = emergencyCareEpisodeService.createAdministrative(newEmergencyCare, institutionId);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PutMapping("/{emergencyCareEpisodeId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> updateAdministrative(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "emergencyCareEpisodeId") Integer emergencyCareEpisodeId,
            @Valid @RequestBody NewEmergencyCareDto body) {
        LOG.debug("Update emergency care administrative episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.emergencyCareDtoToEmergencyCareBo(body);
        List<SnomedDto> reasons = reasonExternalService.addSnomedReasons(body.getReasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));
        newEmergencyCare.setInstitutionId(institutionId);
        newEmergencyCare.setId(emergencyCareEpisodeId);
        Integer result = emergencyCareEpisodeService.updateAdministrative(newEmergencyCare, institutionId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<Integer> createAdult(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody ECAdultGynecologicalDto body) {
        LOG.debug("Add emergency care adult-gynecological episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.adultGynecologicalEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);

        NewRiskFactorsObservationDto riskFactorsObservationDto =
                riskFactorExternalService.saveRiskFactors(body.patientId(), body.riskFactorsObservation());
        newEmergencyCare.setTriageRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));

        List<SnomedDto> reasons = reasonExternalService.addSnomedReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));

		if (newEmergencyCare.getPatient() == null || newEmergencyCare.getPatient().getId() == null)
			newEmergencyCare.setPatient(createEmergencyCareEpisodePatient());

		RiskFactorBo riskFactors = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
		riskFactorExternalService.formatRiskFactors(riskFactors);
		newEmergencyCare = emergencyCareEpisodeService.createAdult(newEmergencyCare, institutionId, riskFactors);
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

        NewRiskFactorsObservationDto riskFactorsObservationDto = triageRiskFactorMapper.fromTriagePediatricDto(body.getTriage());
        riskFactorsObservationDto = riskFactorExternalService.saveRiskFactors(body.patientId(), riskFactorsObservationDto);
        newEmergencyCare.setTriageRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));

        List<SnomedDto> reasons = reasonExternalService.addSnomedReasons(body.reasons());
        newEmergencyCare.setReasons(snomedMapper.toListReasonBo(reasons));

		if (newEmergencyCare.getPatient() == null || newEmergencyCare.getPatient().getId() == null)
			newEmergencyCare.setPatient(createEmergencyCareEpisodePatient());

		RiskFactorBo riskFactors = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
		riskFactorExternalService.formatRiskFactors(riskFactors);
        newEmergencyCare = emergencyCareEpisodeService.createPediatric(newEmergencyCare, institutionId, riskFactors);
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
        	result.add(riskFactorsObservationDto.getBloodGlucose().getId());
		if (riskFactorsObservationDto.getGlycosylatedHemoglobin() != null && riskFactorsObservationDto.getGlycosylatedHemoglobin().getId() != null)
			result.add(riskFactorsObservationDto.getGlycosylatedHemoglobin().getId());
		if (riskFactorsObservationDto.getCardiovascularRisk() != null && riskFactorsObservationDto.getCardiovascularRisk().getId() != null)
			result.add(riskFactorsObservationDto.getCardiovascularRisk().getId());
        LOG.debug("Output -> {}", result);
        return result;
    }

	private PatientECEBo createEmergencyCareEpisodePatient() {
		PatientECEBo result = new PatientECEBo();
		Integer patientId = patientService.addPatient(new Patient(EPatientType.EMERGENCY_CARE_TEMPORARY.getId())).getId();
		result.setId(patientId);
		return result;
	}

}
