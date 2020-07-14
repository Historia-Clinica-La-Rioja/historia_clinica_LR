package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import net.pladema.clinichistory.ips.service.domain.AnthropometricDataBo;

public interface ClinicalObservationGeneralStateService {

    AnthropometricDataBo getLastAnthropometricDataGeneralState(Integer internmentEpisodeId);

    Last2VitalSignsBo getLast2VitalSignsGeneralState(Integer internmentEpisodeId);
}
