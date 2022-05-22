package ar.lamansys.sgh.clinichistory.application.indication.getinternmentepisodeparenteralplans;

import ar.lamansys.sgh.clinichistory.application.ports.ParenteralPlanStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetInternmentEpisodeParenteralPlans {

	private final ParenteralPlanStorage parenteralPlanStorage;

	public List<ParenteralPlanBo> run(Integer internmentEpisodeId) {
		log.debug("Input parameter -> internmentEpisodeId {}", internmentEpisodeId);
		List<ParenteralPlanBo> result = parenteralPlanStorage.getInternmentEpisodeParenteralPlans(internmentEpisodeId);
		log.debug("Output -> {}", result.toString());
		return result;
	}

}
