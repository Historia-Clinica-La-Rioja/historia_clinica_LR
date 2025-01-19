package net.pladema.emergencycare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.hasepisodeadministrativedischarge.HasEpisodeAdministrativeDischarge;
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

@RequiredArgsConstructor
@Tag(name = "Emergency care episodes administrative discharge", description = "Emergency care episodes administrative discharge")
@RequestMapping("/institution/{institutionId}/emergency-care/episodes/{episodeId}/administrative-discharge")
@RestController
public class EmergencyCareEpisodeAdministrativeDischargeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEpisodeMedicalDischargeController.class);

    private final InstitutionExternalService institutionExternalService;
    private final EmergencyCareDischargeMapper emergencyCareDischargeMapper;
    private final EmergencyCareEpisodeAdministrativeDischargeService emergencyCareEpisodeAdministrativeDischargeService;
	private final HasEpisodeAdministrativeDischarge hasEpisodeAdministrativeDischarge;

    @PostMapping
    public ResponseEntity<Boolean> newAdministrativeDischarge(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "episodeId") Integer episodeId,
            @RequestBody AdministrativeDischargeDto administrativeDischargeDto) {
        LOG.debug("New administrative discharge -> episodeId {}, institutionId {}, administrativeDischargeDto {}", episodeId, institutionId, administrativeDischargeDto);
        Integer userId = UserInfo.getCurrentAuditor();
        AdministrativeDischargeBo administrativeDischargeBo = emergencyCareDischargeMapper.toAdministrativeDischargeBo(administrativeDischargeDto, episodeId, userId);
        boolean saved = emergencyCareEpisodeAdministrativeDischargeService.newAdministrativeDischarge(administrativeDischargeBo, institutionId);
        LOG.debug("Output -> {}", saved);
        return ResponseEntity.ok().body(saved);
    }

	@GetMapping
	public ResponseEntity<Boolean> hasAdministrativeDischarge(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "episodeId") Integer episodeId) {
		LOG.debug("Exists administrative discharge -> institutionId {}, episodeId {}", institutionId, episodeId);
		Boolean result = hasEpisodeAdministrativeDischarge.run(episodeId);
		LOG.debug("Output -> result {}", result);
		return ResponseEntity.ok(result);
	}

}
