package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.generalstate.HealthConditionVo;

import java.util.List;

public interface HealthConditionRepositoryCustom {

    List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId);
}
