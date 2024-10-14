package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;

import java.util.List;

public interface GetMostFrequentConceptsService {
	List<SharedSnomedDto> getMostFrequentStudies(Integer institutionId);
}
