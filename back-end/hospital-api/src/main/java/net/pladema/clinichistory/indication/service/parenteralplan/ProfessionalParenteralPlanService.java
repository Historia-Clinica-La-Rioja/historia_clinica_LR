package net.pladema.clinichistory.indication.service.parenteralplan;

import ar.lamansys.sgh.shared.infrastructure.input.service.ParenteralPlanDto;

import java.util.List;

public interface ProfessionalParenteralPlanService {
	List<ParenteralPlanDto> getMostFrequentParenteralPlans(Integer institutionId);
}
