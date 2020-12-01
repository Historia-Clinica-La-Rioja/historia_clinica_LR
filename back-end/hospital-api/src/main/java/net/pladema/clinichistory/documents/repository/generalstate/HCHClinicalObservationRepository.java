package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.service.ips.domain.MapClinicalObservationVo;

public interface HCHClinicalObservationRepository {

    MapClinicalObservationVo getGeneralState(Integer internmentEpisodeId);
}
