package net.pladema.internation.repository.ips;

import net.pladema.internation.service.ips.domain.MapClinicalObservationVo;

public interface ClinicalObservationRepository {

    MapClinicalObservationVo getGeneralStateLastSevenDays(Integer internmentEpisodeId);
}
