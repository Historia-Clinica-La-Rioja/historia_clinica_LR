package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;

public interface EmergencyCareEpisodeAdministrativeDischargeService {

    boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId);

	boolean hasAdministrativeDischarge(Integer episodeId);

}
