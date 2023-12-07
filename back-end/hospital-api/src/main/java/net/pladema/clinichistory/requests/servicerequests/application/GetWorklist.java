package net.pladema.clinichistory.requests.servicerequests.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.WorklistStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.WorklistBo;

@RequiredArgsConstructor
@Service
public class GetWorklist {

	private final WorklistStorage worklistStorage;

	public List<WorklistBo> run(Integer modalityId, Integer institutionId, LocalDateTime start, LocalDateTime end) {
		return worklistStorage.getWorklistByModalityAndInstitution(modalityId, institutionId, start, end);
	}
}
