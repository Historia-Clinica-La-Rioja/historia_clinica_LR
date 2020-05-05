package net.pladema.internation.repository.ips;

import net.pladema.internation.repository.ips.generalstate.AllergyConditionVo;

import java.util.List;

public interface AllergyIntoleranceRepositoryCustom {

    List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId);
}
