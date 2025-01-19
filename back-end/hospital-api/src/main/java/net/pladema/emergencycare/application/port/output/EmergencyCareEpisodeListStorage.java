package net.pladema.emergencycare.application.port.output;

import net.pladema.emergencycare.domain.EmergencyCareEpisodeFilterBo;
import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmergencyCareEpisodeListStorage {

	Page<EmergencyCareBo> getAllEpisodeListByFilter(Integer institutionId, EmergencyCareEpisodeFilterBo filter, Pageable pageable);

}

