package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEAnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.Last2HCERiskFactorsBo;

public interface HCEClinicalObservationService {

    HCEAnthropometricDataBo getLastAnthropometricDataGeneralState(Integer patientId);

    Last2HCERiskFactorsBo getLast2RiskFactorsGeneralState(Integer patientId);
}
