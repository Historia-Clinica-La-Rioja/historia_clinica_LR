package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.ips.service.domain.MapClinicalObservationVo;

public interface HCHClinicalObservationRepository {

    MapClinicalObservationVo getGeneralState(Integer internmentEpisodeId);
}
