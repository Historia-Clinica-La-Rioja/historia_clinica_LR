package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import java.util.List;

public interface  EmergencyCareEpisodeService {

    List<EmergencyCareBo> getAll(Integer institutionId);

    EmergencyCareEpisodeInProgressBo emergencyCareEpisodeInProgress(Integer institutionId, Integer patientId);

    EmergencyCareBo get(Integer episodeId, Integer institutionId);

    EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId);

    EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare, Integer institutionId);

    EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare, Integer institutionId);

    Integer updateAdministrative(EmergencyCareBo newEmergencyCare, Integer institutionId);

    Boolean validateAndSetPatient(Integer episodeId, Integer patientId, Integer institutionId);
}
