package net.pladema.clinichistory.hospitalization.service.indication.parenteralplan;

import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;
import net.pladema.clinichistory.hospitalization.service.indication.parenteralplan.domain.InternmentParenteralPlanBo;

import java.util.List;


public interface InternmentParenteralPlanService {

	Integer add (InternmentParenteralPlanBo parenteralPlanBo, Short sourceTypeId);

	List<ParenteralPlanDto> getInternmentEpisodeParenteralPlans(Integer internmentEpisodeId);
	ParenteralPlanDto getInternmentEpisodeParenteralPlan(Integer parenteralPlanId);

}
