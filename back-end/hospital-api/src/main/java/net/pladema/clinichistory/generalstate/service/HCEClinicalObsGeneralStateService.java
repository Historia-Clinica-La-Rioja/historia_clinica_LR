package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEAnthropometricDataBo;
import net.pladema.clinichistory.generalstate.service.domain.Last2HCEVitalSignsBo;

public interface HCEClinicalObsGeneralStateService {

    HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId);

    Last2HCEVitalSignsBo getLast2VitalSignsGeneralState(Integer patientId);
}
