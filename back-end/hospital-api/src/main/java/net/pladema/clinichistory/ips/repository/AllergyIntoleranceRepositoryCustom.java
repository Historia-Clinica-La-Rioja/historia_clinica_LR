package net.pladema.clinichistory.ips.repository;

import net.pladema.clinichistory.ips.repository.generalstate.AllergyConditionVo;

import java.util.List;

public interface AllergyIntoleranceRepositoryCustom {

    List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId);
}
