package net.pladema.clinichistory.requests.servicerequests.application.port;

import net.pladema.clinichistory.requests.servicerequests.domain.WorklistBo;

import java.util.List;

public interface WorklistStorage {

	List<WorklistBo> getWorklistByModalityAndInstitution(Integer modalityId, Integer institutionId);

	List<WorklistBo> getWorklistByInstitution(Integer institutionId);
}
