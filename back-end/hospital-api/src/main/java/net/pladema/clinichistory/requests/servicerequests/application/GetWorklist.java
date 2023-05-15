package net.pladema.clinichistory.requests.servicerequests.application;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.WorklistStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.WorklistBo;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetWorklist {

	private final WorklistStorage worklistStorage;

	public List<WorklistBo> run(Integer modalityId, Integer institutionId) {
		return worklistStorage.getWorklistByModalityAndInstitution(modalityId, institutionId);
	}
}
