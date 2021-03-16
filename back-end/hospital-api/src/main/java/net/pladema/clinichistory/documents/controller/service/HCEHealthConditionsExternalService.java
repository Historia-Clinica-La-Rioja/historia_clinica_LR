package net.pladema.clinichistory.documents.controller.service;

import net.pladema.clinichistory.documents.controller.dto.HCEPersonalHistoryDto;

import java.util.List;

public interface HCEHealthConditionsExternalService {

	List<HCEPersonalHistoryDto> getFamilyHistories(Integer patientId);

	List<HCEPersonalHistoryDto> getActiveProblems(Integer patientId);

	List<HCEPersonalHistoryDto> getChronicConditions(Integer patientId);
}
