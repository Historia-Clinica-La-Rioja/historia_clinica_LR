package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEMedicationDto;

import java.util.List;

public interface HCEMedicationExternalService {

	List<HCEMedicationDto> getMedication(Integer patientId);
}
