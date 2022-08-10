package ar.lamansys.sgh.clinichistory.application.indication.getInternmentEpisodeParenteralPlan;

import ar.lamansys.sgh.clinichistory.application.ports.ParenteralPlanStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodeParenteralPlan {

	private final ParenteralPlanStorage storage;

	public ParenteralPlanBo run(Integer parenteralPlanId) {
		log.debug("Input parameter -> parenteralPlanId {}", parenteralPlanId);
		ParenteralPlanBo result = storage.findById(parenteralPlanId)
				.orElseThrow(() -> new EntityNotFoundException("parenteralPlan.invalid.id"));
		log.debug("Output -> {}", result.toString());
		return result;
	}
}