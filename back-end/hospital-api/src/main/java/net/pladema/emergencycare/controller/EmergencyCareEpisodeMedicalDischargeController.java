package net.pladema.emergencycare.controller;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.emergencycare.controller.dto.AMedicalDischargeDto;
import net.pladema.emergencycare.controller.dto.VMedicalDischargeDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareDischargeMapper;
import net.pladema.emergencycare.repository.entity.EmergencyCareState;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeStateService;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/medical-discharge")
@Tag(name = "Emergency care episodes discharge", description = "Emergency care episodes discharge")
public class EmergencyCareEpisodeMedicalDischargeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);

    private final EmergencyCareEpisodeDischargeService emergencyCareEpisodeDischargeService;
    private final EmergencyCareDischargeMapper emergencyCareDischargeMapper;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final PatientExternalService patientExternalService;
    private final EmergencyCareEpisodeService emergencyCareEpisodeService;
    private final EmergencyCareEpisodeStateService emergencyCareEpisodeStateService;
    private final InstitutionExternalService institutionExternalService;
	private final HospitalApiPublisher hospitalApiPublisher;

    EmergencyCareEpisodeMedicalDischargeController(EmergencyCareEpisodeDischargeService emergencyCareEpisodeDischargeService, EmergencyCareDischargeMapper emergencyCareDischargeMapper, HealthcareProfessionalExternalService healthcareProfessionalExternalService, PatientExternalService patientExternalService, EmergencyCareEpisodeService emergencyCareEpisodeService, EmergencyCareEpisodeStateService emergencyCareEpisodeStateService, InstitutionExternalService institutionExternalService, HospitalApiPublisher hospitalApiPublisher) {
        this.emergencyCareEpisodeDischargeService = emergencyCareEpisodeDischargeService;
        this.emergencyCareDischargeMapper = emergencyCareDischargeMapper;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.patientExternalService = patientExternalService;
        this.emergencyCareEpisodeService = emergencyCareEpisodeService;
        this.emergencyCareEpisodeStateService = emergencyCareEpisodeStateService;
        this.institutionExternalService = institutionExternalService;
		this.hospitalApiPublisher = hospitalApiPublisher;
	}

    @Transactional // Transaccion compleja
    @PostMapping
    public ResponseEntity<Boolean> newMedicalDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId,
            @RequestBody AMedicalDischargeDto medicalDischargeDto) {
        LOG.debug("New medical discharge -> episodeId {}, institutionId {}, medicalDischargeDto {}", episodeId, institutionId, medicalDischargeDto);

        Integer medicalDischargeBy = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());

        EmergencyCareBo emergencyCareBo = emergencyCareEpisodeService.get(episodeId, institutionId);
        if (emergencyCareBo.getPatient() == null)
            throw new NotFoundException("El episodio debe tener asociado un paciente", "El episodio debe tener asociado un paciente");

        Integer patientId = emergencyCareBo.getPatient().getId();
        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfo =  new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        MedicalDischargeBo medicalDischargeBo = emergencyCareDischargeMapper.toMedicalDischargeBo(medicalDischargeDto,medicalDischargeBy,patientInfo, episodeId);
        boolean saved = emergencyCareEpisodeDischargeService.newMedicalDischarge(medicalDischargeBo, institutionZoneId, institutionId);
        emergencyCareEpisodeStateService.changeState(episodeId, institutionId, EmergencyCareState.CON_ALTA_MEDICA, null);
		hospitalApiPublisher.publish(medicalDischargeBo.getPatientId(), institutionId, EHospitalApiTopicDto.ALTA_MEDICA);
        LOG.debug("Output -> {}", saved);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping()
    public ResponseEntity<VMedicalDischargeDto> getMedicalDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId) {
        LOG.debug("Change emergency care state -> episodeId {}, institutionId {}", episodeId, institutionId);
        EpisodeDischargeBo episodeDischargeBo = emergencyCareEpisodeDischargeService.getDischarge(episodeId);
        VMedicalDischargeDto medicalDischargeDto = emergencyCareDischargeMapper.toMedicalDischargeDto(episodeDischargeBo);
        medicalDischargeDto.setSnomedPtProblems(episodeDischargeBo.getProblems().stream().map(SnomedBo::getPt).collect(Collectors.toList()));
        LOG.debug("Output -> {}", medicalDischargeDto);
        return ResponseEntity.ok().body(medicalDischargeDto);
    }

}