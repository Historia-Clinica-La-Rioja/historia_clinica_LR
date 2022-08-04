package net.pladema.emergencycare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.emergencycare.controller.dto.AdministrativeDischargeDto;
import net.pladema.emergencycare.controller.mapper.EmergencyCareDischargeMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeAdministrativeDischargeService;
import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import ar.lamansys.sgx.shared.security.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;

@RestController
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/administrative-discharge")
@Tag(name = "Emergency care episodes administrative discharge", description = "Emergency care episodes administrative discharge")
public class EmergencyCareEpisodeAdministrativeDischargeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);

    private final InstitutionExternalService institutionExternalService;
    private final EmergencyCareDischargeMapper emergencyCareDischargeMapper;
    private final EmergencyCareEpisodeAdministrativeDischargeService emergencyCareEpisodeAdministrativeDischargeService;

    public EmergencyCareEpisodeAdministrativeDischargeController(InstitutionExternalService institutionExternalService, EmergencyCareDischargeMapper emergencyCareDischargeMapper, EmergencyCareEpisodeAdministrativeDischargeService emergencyCareEpisodeAdministrativeDischargeService) {
        this.institutionExternalService = institutionExternalService;
        this.emergencyCareDischargeMapper = emergencyCareDischargeMapper;
        this.emergencyCareEpisodeAdministrativeDischargeService = emergencyCareEpisodeAdministrativeDischargeService;
    }


    @PostMapping
    public ResponseEntity<Boolean> newAdministrativeDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId,
            @RequestBody AdministrativeDischargeDto administrativeDischargeDto) {
        LOG.debug("New administrative discharge -> episodeId {}, institutionId {}, administrativeDischargeDto {}", episodeId, institutionId, administrativeDischargeDto);
        Integer userId = UserInfo.getCurrentAuditor();
        AdministrativeDischargeBo administrativeDischargeBo = emergencyCareDischargeMapper.toAdministrativeDischargeBo(administrativeDischargeDto, episodeId, userId);
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        boolean saved = emergencyCareEpisodeAdministrativeDischargeService.newAdministrativeDischarge(administrativeDischargeBo, institutionId, institutionZoneId);
        LOG.debug("Output -> {}", saved);
        return ResponseEntity.ok().body(saved);
    }


    @PostMapping("/absence")
    public ResponseEntity<Boolean> newAdministrativeDischargeByAbsence(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId) {
        LOG.debug("New administrative discharge by absence -> episodeId {}, institutionId {}", episodeId, institutionId);
        Integer userId = UserInfo.getCurrentAuditor();
        ZoneId institutionZoneId = institutionExternalService.getTimezone(institutionId);
        boolean saved = emergencyCareEpisodeAdministrativeDischargeService.newAdministrativeDischargeByAbsence(episodeId, institutionId, userId, institutionZoneId);
        LOG.debug("Output -> {}", saved);
        return ResponseEntity.ok().body(saved);
    }
}
