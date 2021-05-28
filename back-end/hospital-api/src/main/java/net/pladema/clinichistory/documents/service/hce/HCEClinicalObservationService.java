package net.pladema.clinichistory.documents.service.hce;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCEVitalSignsBo;

public interface HCEClinicalObservationService {

    HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId);

    Last2HCEVitalSignsBo getLast2VitalSignsGeneralState(Integer patientId);
}
