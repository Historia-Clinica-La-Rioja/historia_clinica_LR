package net.pladema.clinichistory.requests.servicerequests.repository;

import java.util.List;

public interface StudyWorkListRepository {
	List<Object[]> execute(Integer institutionId, List<String> categories, List<Short> sourceTypeIds, String statusId, Short documentType);
}
