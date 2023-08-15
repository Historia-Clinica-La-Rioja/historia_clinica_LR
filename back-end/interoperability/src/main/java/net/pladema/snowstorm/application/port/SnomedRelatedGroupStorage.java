package net.pladema.snowstorm.application.port;

import net.pladema.snowstorm.services.domain.SnomedRelatedGroupBo;

import java.util.List;

public interface SnomedRelatedGroupStorage {

	List<SnomedRelatedGroupBo> getPracticesByInstitution(Integer institutionId);

}
