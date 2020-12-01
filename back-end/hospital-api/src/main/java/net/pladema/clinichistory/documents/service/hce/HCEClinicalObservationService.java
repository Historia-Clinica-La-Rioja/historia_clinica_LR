package net.pladema.clinichistory.documents.service.hce;

import net.pladema.clinichistory.documents.service.hce.domain.HCEAnthropometricDataBo;
import net.pladema.clinichistory.documents.service.hce.domain.Last2HCEVitalSignsBo;

public interface HCEClinicalObservationService {

    HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId);

    Last2HCEVitalSignsBo getLast2VitalSignsGeneralState(Integer patientId);
}
