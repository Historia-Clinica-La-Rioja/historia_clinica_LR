package net.pladema.internation.repository.ips;

import net.pladema.internation.service.domain.ips.MapVitalSigns;

public interface ObservationVitalSignRepositoryCustom {

    MapVitalSigns getVitalSignsGeneralStateLastSevenDays(Integer internmentEpisodeId);
}
