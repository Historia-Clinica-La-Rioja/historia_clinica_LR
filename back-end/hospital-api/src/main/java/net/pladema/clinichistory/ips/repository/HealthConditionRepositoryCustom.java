package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.generalstate.HealthConditionVo;

import java.util.List;

public interface HealthConditionRepositoryCustom {

    List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId);
}
