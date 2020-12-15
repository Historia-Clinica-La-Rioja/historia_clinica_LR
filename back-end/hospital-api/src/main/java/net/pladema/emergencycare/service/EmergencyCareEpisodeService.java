package net.pladema.emergencycare.service;

import net.pladema.emergencycare.service.domain.EmergencyCareBo;
import net.pladema.sgx.masterdata.repository.MasterDataProjection;

import java.util.List;

public interface EmergencyCareEpisodeService {

    List<EmergencyCareBo> getAll(Integer institutionId);

    EmergencyCareBo get(Integer episodeId);

    EmergencyCareBo createAdministrative(EmergencyCareBo newEmergencyCare);

    EmergencyCareBo createAdult(EmergencyCareBo newEmergencyCare);

    EmergencyCareBo createPediatric(EmergencyCareBo newEmergencyCare);

    MasterDataProjection getState(Integer episodeId);

    Boolean changeState(Integer episodeId, Short emergencyCareStateId, Integer doctorsOfficeId);
}
