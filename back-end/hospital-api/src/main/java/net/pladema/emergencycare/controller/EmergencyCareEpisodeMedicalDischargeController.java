package net.pladema.emergencycare.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.CreateMedicalDischarge;
import net.pladema.emergencycare.controller.dto.AMedicalDischargeDto;
import net.pladema.emergencycare.controller.dto.VMedicalDischargeDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareDischargeMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeDischargeService;
import net.pladema.emergencycare.service.domain.EpisodeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Emergency care episodes discharge", description = "Emergency care episodes discharge")
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/medical-discharge")
@Slf4j
@RequiredArgsConstructor
@RestController
public class EmergencyCareEpisodeMedicalDischargeController {

    private final EmergencyCareEpisodeDischargeService emergencyCareEpisodeDischargeService;
    private final EmergencyCareDischargeMapper emergencyCareDischargeMapper;
    private final CreateMedicalDischarge createMedicalDischarge;

    @PostMapping
    public ResponseEntity<Boolean> newMedicalDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId,
            @RequestBody AMedicalDischargeDto medicalDischargeDto) {
        log.debug("New medical discharge -> episodeId {}, institutionId {}, medicalDischargeDto {}", episodeId, institutionId, medicalDischargeDto);

        MedicalDischargeBo medicalDischargeBo = emergencyCareDischargeMapper.toMedicalDischargeBo(medicalDischargeDto);
        medicalDischargeBo.setInstitutionId(institutionId);
        medicalDischargeBo.setSourceId(episodeId);

        boolean saved = createMedicalDischarge.run(medicalDischargeBo);

        log.debug("Output -> {}", saved);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping
    public ResponseEntity<VMedicalDischargeDto> getMedicalDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId) {
        log.debug("Change emergency care state -> episodeId {}, institutionId {}", episodeId, institutionId);
        EpisodeDischargeBo episodeDischargeBo = emergencyCareEpisodeDischargeService.getDischarge(episodeId);
        VMedicalDischargeDto medicalDischargeDto = emergencyCareDischargeMapper.toMedicalDischargeDto(episodeDischargeBo);
        medicalDischargeDto.setSnomedPtProblems(episodeDischargeBo.getProblems().stream().map(SnomedBo::getPt).collect(Collectors.toList()));
        log.debug("Output -> {}", medicalDischargeDto);
        return ResponseEntity.ok().body(medicalDischargeDto);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> hasMedicalDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId) {
        log.debug("Change emergency care state -> episodeId {}, institutionId {}", episodeId, institutionId);
        Boolean result = emergencyCareEpisodeDischargeService.hasMedicalDischarge(episodeId);
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }

}