package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEAllergyDto;

import java.util.List;

public interface HCEAllergyExternalService {

	List<HCEAllergyDto> getAllergies(Integer patientId);
}
