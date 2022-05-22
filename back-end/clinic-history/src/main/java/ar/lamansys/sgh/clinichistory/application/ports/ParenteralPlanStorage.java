package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.ParenteralPlanBo;

import java.util.List;

public interface ParenteralPlanStorage {

	Integer createParenteralPlan (ParenteralPlanBo parenteralPlan);

	List<ParenteralPlanBo> getInternmentEpisodeParenteralPlans(Integer internmentEpisodeId);

}
