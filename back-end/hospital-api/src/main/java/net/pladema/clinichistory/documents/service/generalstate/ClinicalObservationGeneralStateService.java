package net.pladema.clinichistory.documents.service.generalstate;

import net.pladema.clinichistory.hospitalization.service.domain.Last2VitalSignsBo;
import net.pladema.clinichistory.documents.service.ips.domain.AnthropometricDataBo;

public interface ClinicalObservationGeneralStateService {

    AnthropometricDataBo getLastAnthropometricDataGeneralState(Integer internmentEpisodeId);

    Last2VitalSignsBo getLast2VitalSignsGeneralState(Integer internmentEpisodeId);
}
