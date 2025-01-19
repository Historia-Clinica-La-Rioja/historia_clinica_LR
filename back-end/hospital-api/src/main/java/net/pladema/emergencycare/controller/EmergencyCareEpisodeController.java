package net.pladema.emergencycare.controller;

import ar.lamansys.sgh.clinichistory.application.fetchEmergencyCareEpisodeState.FetchECStateClinicalObservations;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.service.RiskFactorExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.GetAllEpisodeListByFilter;
import net.pladema.emergencycare.application.getemercencycarelastattentionplace.GetEmergencyCareEpisodeLastAttentionPlace;
import net.pladema.emergencycare.controller.dto.AEmergencyCarePatientDto;
import net.pladema.emergencycare.controller.dto.ECAdministrativeDto;
import net.pladema.emergencycare.controller.dto.ECAdultGynecologicalDto;
import net.pladema.emergencycare.controller.dto.ECPediatricDto;
import net.pladema.emergencycare.controller.dto.EmergencyCareListDto;
import net.pladema.emergencycare.controller.dto.NewEmergencyCareDto;
import net.pladema.emergencycare.controller.exceptions.SaveEmergencyCareEpisodeExceptionEnum;
import net.pladema.emergencycare.controller.exceptions.SaveEmergencyCareEpisodeException;
import net.pladema.emergencycare.controller.mapper.EmergencyCareMapper;
import net.pladema.emergencycare.controller.mapper.TriageRiskFactorMapper;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeFilterBo;
import net.pladema.emergencycare.controller.dto.EmergencyCareEpisodeFilterDto;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareEpisodeAttentionPlaceDto;
import net.pladema.emergencycare.infrastructure.input.rest.mapper.EmergencyCareEpisodeAttentionPlaceMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes")
@Tag(name = "Emergency care episodes", description = "Emergency care episodes")
@Validated
public class EmergencyCareEpisodeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeController.class);

    private final EmergencyCareEpisodeService emergencyCareEpisodeService;

    private final EmergencyCareMapper emergencyCareMapper;

    private final RiskFactorExternalService riskFactorExternalService;

	private final PatientService patientService;

    private final TriageRiskFactorMapper triageRiskFactorMapper;

    private final LocalDateMapper localDateMapper;

	private final RiskFactorMapper riskFactorMapper;

	private final FetchECStateClinicalObservations fetchECStateClinicalObservations;

	private final ObjectMapper objectMapper;

	private final GetAllEpisodeListByFilter getAllEpisodeListByFilter;

	private final GetEmergencyCareEpisodeLastAttentionPlace getEmergencyCareEpisodeLastAttentionPlace;

	private final EmergencyCareEpisodeAttentionPlaceMapper emergencyCareEpisodeAttentionPlaceMapper;

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public PageDto<EmergencyCareListDto> getAll(@PathVariable(name = "institutionId") Integer institutionId,												@RequestParam(name = "pageNumber") Integer pageNumber,
												@RequestParam(name = "pageSize") Integer pageSize,
												@RequestParam(name = "filter") String filterData) throws JsonProcessingException {
		LOG.debug("Input parameters -> institutionId {}, pageNumber {}, pageSize {}", institutionId, pageNumber, pageSize);
		EmergencyCareEpisodeFilterBo filter = emergencyCareMapper.fromEmergencyCareEpisodeFilterDto(objectMapper.readValue(filterData, EmergencyCareEpisodeFilterDto.class));
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<EmergencyCareBo> episodes = getAllEpisodeListByFilter.run(institutionId, filter, pageable);
		Page<EmergencyCareListDto> result = episodes.map(emergencyCareMapper::toEmergencyCareListDto);
		LOG.debug("Output -> {}", result);
		return PageDto.fromPage(result);
	}


    @Transactional
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> createAdministrative(
            @PathVariable(name = "institutionId") Integer institutionId,
            @Valid @RequestBody ECAdministrativeDto body) {
        LOG.debug("Add emergency care administrative episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.administrativeEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);
		if (newEmergencyCare.getPatient().getId() == null) {
			PatientECEBo temporaryPatient = createEmergencyCareEpisodeTemporaryPatient(body.getAdministrative().getPatient());
			newEmergencyCare.setPatient(temporaryPatient);
		}
        newEmergencyCare = emergencyCareEpisodeService.createAdministrative(newEmergencyCare, institutionId);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PutMapping("/{emergencyCareEpisodeId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> updateAdministrative(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "emergencyCareEpisodeId") Integer emergencyCareEpisodeId,
            @Valid @RequestBody NewEmergencyCareDto body) {
        LOG.debug("Update emergency care administrative episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.emergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);
        newEmergencyCare.setId(emergencyCareEpisodeId);
        Integer result = emergencyCareEpisodeService.updateAdministrative(newEmergencyCare, institutionId);
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/adult-gynecological")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> createAdult(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestBody ECAdultGynecologicalDto body) {
        LOG.debug("Add emergency care adult-gynecological episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.adultGynecologicalEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);

		if (newEmergencyCare.getPatient().getId() == null) {
			PatientECEBo temporaryPatient = createEmergencyCareEpisodeTemporaryPatient(body.getAdministrative().getPatient());
			newEmergencyCare.setPatient(temporaryPatient);
		}

        NewRiskFactorsObservationDto riskFactorsObservationDto =
                riskFactorExternalService.saveRiskFactors(newEmergencyCare.getPatient().getId(), body.riskFactorsObservation());
        newEmergencyCare.setTriageRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));

		RiskFactorBo riskFactors = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
		riskFactorExternalService.formatRiskFactors(riskFactors);
		newEmergencyCare = emergencyCareEpisodeService.createAdult(newEmergencyCare, institutionId, riskFactors);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @Transactional
    @PostMapping("/pediatric")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> createPediatric(@PathVariable(name = "institutionId") Integer institutionId,
                                                   @RequestBody ECPediatricDto body) {
        LOG.debug("Add emergency care pediatric episode -> institutionId {}, body {}", institutionId, body);
        EmergencyCareBo newEmergencyCare = emergencyCareMapper.pediatricEmergencyCareDtoToEmergencyCareBo(body);
        newEmergencyCare.setInstitutionId(institutionId);

		if (newEmergencyCare.getPatient().getId() == null) {
			PatientECEBo temporaryPatient = createEmergencyCareEpisodeTemporaryPatient(body.getAdministrative().getPatient());
			newEmergencyCare.setPatient(temporaryPatient);
		}

        NewRiskFactorsObservationDto riskFactorsObservationDto = triageRiskFactorMapper.fromTriagePediatricDto(body.getTriage());
        riskFactorsObservationDto = riskFactorExternalService.saveRiskFactors(newEmergencyCare.getPatient().getId(), riskFactorsObservationDto);
        newEmergencyCare.setTriageRiskFactorIds(getRiskFactorIds(riskFactorsObservationDto));

		RiskFactorBo riskFactors = riskFactorMapper.fromRiskFactorsObservationDto(riskFactorsObservationDto);
		riskFactorExternalService.formatRiskFactors(riskFactors);
        newEmergencyCare = emergencyCareEpisodeService.createPediatric(newEmergencyCare, institutionId, riskFactors);
        Integer result = newEmergencyCare.getId();
        LOG.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{episodeId}/creation-date")
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<DateTimeDto> getCreationDate(@PathVariable(name = "institutionId") Integer institutionId,
                                                       @PathVariable(name = "episodeId") Integer episodeId) {
        LOG.debug("Get episode creation date -> institutionId {}, episodeId {}", institutionId, episodeId);
        EmergencyCareBo emergencyCareBo = emergencyCareEpisodeService.get(episodeId,institutionId);
        LocalDateTime creationDate = emergencyCareBo.getCreatedOn();
        DateTimeDto output = localDateMapper.toDateTimeDto(creationDate);
        LOG.debug("Output -> {}", output);
        return ResponseEntity.ok().body(output);
    }

	@GetMapping("/{episodeId}/has-evolution-note")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> hasEvolutionNote(@PathVariable(name = "institutionId") Integer institutionId,
													   @PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Get episode creation date -> institutionId {}, episodeId {}", institutionId, episodeId);
		Boolean output = emergencyCareEpisodeService.hasEvolutionNote(episodeId);
		LOG.debug("Output -> {}", output);
		return ResponseEntity.ok().body(output);
	}

	@GetMapping("/{episodeId}/general/riskFactors")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<RiskFactorDto> getRiskFactorsGeneralState(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Input parameters -> institutionId {}, episodeId {}", institutionId, episodeId);
		RiskFactorDto result = riskFactorMapper.fromRiskFactorBo(fetchECStateClinicalObservations.getLastRiskFactorsGeneralState(episodeId));
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping("/{emergencyCareEpisodeId}/updatePatientDescription")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<Boolean> updatePatientDescription(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "emergencyCareEpisodeId") Integer emergencyCareEpisodeId,
			@RequestBody String patientDescription) {
		LOG.debug("Update emergency care patient description -> institutionId {}, emergencyCareEpisodeId {}, patientDescription {}", institutionId, emergencyCareEpisodeId, patientDescription);
		Boolean result = emergencyCareEpisodeService.updatePatientDescription(emergencyCareEpisodeId, patientDescription);
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/{episodeId}/last-attention-place")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public EmergencyCareEpisodeAttentionPlaceDto getLastAttentionPlace(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Input parameters getLastAttentionPlace -> institutionId {}, episodeId {}", institutionId, episodeId);
		EmergencyCareEpisodeAttentionPlaceDto result =  emergencyCareEpisodeAttentionPlaceMapper.toDto(
				getEmergencyCareEpisodeLastAttentionPlace.run(episodeId));
		LOG.debug("Output -> {}", result);
		return result;
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

	private PatientECEBo createEmergencyCareEpisodeTemporaryPatient(AEmergencyCarePatientDto patient) {
		assertTemporaryPatient(patient.getPatientDescription());
		PatientECEBo result = new PatientECEBo();
		Integer patientId = patientService.addPatient(new Patient(EPatientType.EMERGENCY_CARE_TEMPORARY.getId(), EAuditType.UNAUDITED.getId())).getId();
		result.setId(patientId);
		result.setPatientDescription(patient.getPatientDescription());
		return result;
	}

	private void assertTemporaryPatient(String patientDescription) {
		boolean hasNotDescription = patientDescription == null || patientDescription.isEmpty();
		if (hasNotDescription)
			throw new SaveEmergencyCareEpisodeException(SaveEmergencyCareEpisodeExceptionEnum.PATIENT_DESCRIPTION,"No se puede crear un episodio de guardia con paciente temporal sin una descripcion identificatoria del paciente");
	}
}
