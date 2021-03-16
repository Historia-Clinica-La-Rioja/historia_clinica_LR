package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEAnthropometricDataDto;
import net.pladema.clinichistory.documents.controller.dto.HCELast2VitalSignsDto;

public interface HCEClinicalObservationExternalService {

	HCEAnthropometricDataDto getLastAnthropometricDataGeneralState(Integer patientId);

	HCELast2VitalSignsDto getLast2VitalSignsGeneralState(Integer patientId);
}
