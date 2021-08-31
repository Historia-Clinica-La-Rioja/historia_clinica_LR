package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;

import java.util.List;

public interface HCHHealthConditionRepository {

    List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId);
}
