package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCELast2VitalSignsDto;

public interface HCEClinicalObservationExternalService {

	HCEAnthropometricDataDto getLastAnthropometricDataGeneralState(Integer patientId);

	HCELast2VitalSignsDto getLast2VitalSignsGeneralState(Integer patientId);
}
