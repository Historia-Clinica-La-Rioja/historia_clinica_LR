package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.AllergyConditionVo;

import java.util.List;

public interface HCHAllergyIntoleranceRepository {

    List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId);
}
