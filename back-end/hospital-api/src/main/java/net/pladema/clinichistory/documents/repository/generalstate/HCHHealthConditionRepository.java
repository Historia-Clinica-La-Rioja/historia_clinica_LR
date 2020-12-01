package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.HealthConditionVo;

import java.util.List;

public interface HCHHealthConditionRepository {

    List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId);
}
