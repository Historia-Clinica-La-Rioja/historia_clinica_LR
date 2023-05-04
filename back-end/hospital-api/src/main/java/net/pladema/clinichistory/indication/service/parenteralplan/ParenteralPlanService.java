package net.pladema.clinichistory.indication.service.parenteralplan;

import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import net.pladema.clinichistory.indication.service.parenteralplan.domain.GenericParenteralPlanBo;

import java.util.List;


public interface ParenteralPlanService {

	Integer add(GenericParenteralPlanBo parenteralPlanBo);

	List<ParenteralPlanDto> getEpisodeParenteralPlans(Integer internmentEpisodeId, Short sourceTypeId);
	ParenteralPlanDto getParenteralPlan(Integer parenteralPlanId);

}
