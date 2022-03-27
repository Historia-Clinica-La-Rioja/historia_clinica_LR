package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCELast2RiskFactorsDto;

public interface HCEClinicalObservationExternalService {

	HCEAnthropometricDataDto getLastAnthropometricDataGeneralState(Integer patientId);

	HCELast2RiskFactorsDto getLast2RiskFactorsGeneralState(Integer patientId);
}
