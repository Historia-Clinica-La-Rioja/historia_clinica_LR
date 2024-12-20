package net.pladema.emergencycare.service;

import java.util.Collection;
import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;
import net.pladema.emergencycare.repository.EmergencyEpisodeAttendSectorType;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;

public interface EmergencyCareMasterDataService {

    List<EEmergencyCareType> findAllType();

    List<EEmergencyCareEntrance> findAllEntrance();

	List<EmergencyEpisodeAttendSectorType> getEmergencyEpisodeSectorType();

	List<EEmergencyCareState> getEmergencyCareStates();

	List<EBlockAttentionPlaceReason> getAttentionPlaceBlockReasons();

	List<EIsolationType> getIsolationTypes();

	Collection<EIsolationCriticality> getCriticalities();
}
