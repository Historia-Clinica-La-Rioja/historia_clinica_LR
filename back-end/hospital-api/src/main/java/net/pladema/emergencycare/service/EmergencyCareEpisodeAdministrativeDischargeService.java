package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;

import java.time.ZoneId;

public interface EmergencyCareEpisodeAdministrativeDischargeService {

    boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId, ZoneId institutionZoneId);

    boolean newAdministrativeDischargeByAbsence(Integer episodeId, Integer institutionId, Integer userdId, ZoneId institutionZoneId);
}
