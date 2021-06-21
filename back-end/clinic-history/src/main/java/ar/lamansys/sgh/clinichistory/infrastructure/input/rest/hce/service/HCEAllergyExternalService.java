package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEAllergyDto;

import java.util.List;

public interface HCEAllergyExternalService {

	List<HCEAllergyDto> getAllergies(Integer patientId);
}
