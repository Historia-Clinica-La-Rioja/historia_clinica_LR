package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.AdministrativeDischargeBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;

import java.time.ZoneId;

public interface EmergencyCareEpisodeDischargeService {

    boolean newMedicalDischarge(MedicalDischargeBo medicalDischargeBo);

    boolean newAdministrativeDischarge(AdministrativeDischargeBo administrativeDischargeBo, Integer institutionId);

    boolean newAdministrativeDischargeByAbsence(Integer episodeId, Integer institutionId, Integer userdId, ZoneId institutionZoneId);
}
