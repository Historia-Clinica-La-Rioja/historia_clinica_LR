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

	public List<ParenteralPlanBo> run(Integer internmentEpisodeId, Short sourceTypeId) {
		log.debug("Input parameter -> internmentEpisodeId {}, sourceTypeId {}", internmentEpisodeId, sourceTypeId);
		List<ParenteralPlanBo> result = parenteralPlanStorage.getInternmentEpisodeParenteralPlans(internmentEpisodeId, sourceTypeId);
		log.debug("Output -> {}", result.toString());
		return result;
	}

}
