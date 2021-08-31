package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEPersonalHistoryDto;

import java.util.List;

public interface HCEHealthConditionsExternalService {

	List<HCEPersonalHistoryDto> getFamilyHistories(Integer patientId);

	List<HCEPersonalHistoryDto> getActivePersonalHistories(Integer patientId);
}
