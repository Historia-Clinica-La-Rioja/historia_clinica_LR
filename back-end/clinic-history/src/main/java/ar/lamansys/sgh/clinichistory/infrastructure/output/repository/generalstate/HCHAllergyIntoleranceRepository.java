package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.AllergyConditionVo;

import java.util.List;

public interface HCHAllergyIntoleranceRepository {

    List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId);
}
