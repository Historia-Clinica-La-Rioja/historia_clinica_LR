package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import java.util.List;

public interface  EmergencyCareEpisodeService {

    List<EmergencyCareBo> getAll(Integer institutionId);

    EmergencyCareBo get(Integer episodeId, Integer institutionId);

    EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare);

    EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare);

    EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare);

    Boolean setPatient(Integer episodeId, Integer patientId);
}
