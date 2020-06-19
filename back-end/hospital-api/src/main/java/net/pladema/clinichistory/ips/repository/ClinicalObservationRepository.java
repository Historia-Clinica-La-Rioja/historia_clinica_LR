package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.service.domain.MapClinicalObservationVo;

public interface ClinicalObservationRepository {

    MapClinicalObservationVo getGeneralState(Integer internmentEpisodeId);
}
