package net.pladema.establishment.application.port;

import java.util.List;

public interface RuleStorage {

	boolean isAnyClinicalSpecialtyRegulated(List<Integer> clinicalSpecialtyIds, Integer institutionId);

	boolean isPracticeRegulated(Integer practiceId, Integer institutionId);

}
