package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.emergencyCareEpisodeState;

import ar.lamansys.sgh.clinichistory.domain.ips.MapClinicalObservationVo;

import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyCareClinicalObservationsRepository {

	MapClinicalObservationVo getGeneralState(Integer episodeId);
}
