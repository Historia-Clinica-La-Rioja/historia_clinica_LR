package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.AllergyConditionVo;

import java.util.List;

public interface HCHAllergyIntoleranceRepository {

    List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId);
}
