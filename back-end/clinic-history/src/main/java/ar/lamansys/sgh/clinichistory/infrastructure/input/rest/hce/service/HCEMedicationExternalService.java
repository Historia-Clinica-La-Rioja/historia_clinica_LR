package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEMedicationDto;

import java.util.List;

public interface HCEMedicationExternalService {

	List<HCEMedicationDto> getMedication(Integer patientId);
}
