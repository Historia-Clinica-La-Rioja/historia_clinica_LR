package net.pladema.clinichistory.requests.servicerequests.application.port;

import java.time.LocalDateTime;
import java.util.List;

import net.pladema.clinichistory.requests.servicerequests.domain.WorklistBo;

public interface WorklistStorage {

	List<WorklistBo> getWorklistByModalityAndInstitution(Integer modalityId, Integer institutionId, LocalDateTime start, LocalDateTime end);
}
