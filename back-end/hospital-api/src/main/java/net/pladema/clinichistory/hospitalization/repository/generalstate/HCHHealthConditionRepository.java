package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.HealthConditionVo;

import java.util.List;

public interface HCHHealthConditionRepository {

    List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId);
}
