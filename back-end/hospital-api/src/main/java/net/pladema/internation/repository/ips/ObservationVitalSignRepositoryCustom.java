package net.pladema.internation.repository.ips;

import net.pladema.internation.service.domain.ips.MapClinicalObservationVo;

public interface ObservationVitalSignRepositoryCustom {

    MapClinicalObservationVo getVitalSignsGeneralStateLastSevenDays(Integer internmentEpisodeId);
}
