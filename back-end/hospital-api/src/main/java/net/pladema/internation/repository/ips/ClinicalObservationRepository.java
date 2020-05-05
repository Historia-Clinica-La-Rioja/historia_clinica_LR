package net.pladema.internation.repository.ips;

import net.pladema.internation.service.domain.ips.MapClinicalObservationVo;

public interface ClinicalObservationRepository {

    MapClinicalObservationVo getGeneralStateLastSevenDays(Integer internmentEpisodeId);
}
