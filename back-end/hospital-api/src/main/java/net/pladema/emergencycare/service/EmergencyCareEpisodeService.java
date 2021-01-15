package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.List;

public interface  EmergencyCareEpisodeService {

    List<EmergencyCareBo> getAll(Integer institutionId);

    EmergencyCareBo get(Integer episodeId, Integer institutionId);

    EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare);

    EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare);

    EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare);

    EEmergencyCareState getState(Integer episodeId, Integer institutionId);

    Boolean changeState(Integer episodeId, Short emergencyCareStateId, Integer doctorsOfficeId);

    Boolean setPatient(Integer episodeId, Integer patientId);
}
