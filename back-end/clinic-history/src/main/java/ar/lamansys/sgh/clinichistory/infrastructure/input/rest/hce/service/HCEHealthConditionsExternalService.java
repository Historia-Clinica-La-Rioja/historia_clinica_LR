package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.service;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto.HCEHealthConditionDto;

import java.util.List;

public interface HCEHealthConditionsExternalService {

	List<HCEHealthConditionDto> getFamilyHistories(Integer patientId);

	List<HCEHealthConditionDto> getSummaryProblems(Integer patientId);
}
