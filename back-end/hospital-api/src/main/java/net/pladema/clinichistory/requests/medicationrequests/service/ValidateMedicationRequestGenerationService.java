package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberValidationResponseDto;

public interface ValidateMedicationRequestGenerationService {

	ProfessionalLicenseNumberValidationResponseDto execute(Integer userId, ProfessionalDto healthcareProfessionalData);
}
