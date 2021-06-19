package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.AllergyConditionVo;

import java.util.List;

public interface HCHAllergyIntoleranceRepository {

    List<AllergyConditionVo> findGeneralState(Integer internmentEpisodeId);
}
