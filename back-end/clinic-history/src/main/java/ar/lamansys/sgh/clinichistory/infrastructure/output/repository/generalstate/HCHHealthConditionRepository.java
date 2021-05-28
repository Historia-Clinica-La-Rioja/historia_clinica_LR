package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.HealthConditionVo;

import java.util.List;

public interface HCHHealthConditionRepository {

    List<HealthConditionVo> findGeneralState(Integer internmentEpisodeId);
}
