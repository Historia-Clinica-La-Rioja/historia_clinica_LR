package net.pladema.emergencycare.service;

import net.pladema.emergencycare.repository.EmergencyEpisodeAttendSectorType;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;

import java.util.List;

public interface EmergencyCareMasterDataService {

    List<EEmergencyCareType> findAllType();

    List<EEmergencyCareEntrance> findAllEntrance();

	List<EmergencyEpisodeAttendSectorType> getEmergencyEpisodeSectorType();

	List<EEmergencyCareState> getEmergencyCareStates();
}
