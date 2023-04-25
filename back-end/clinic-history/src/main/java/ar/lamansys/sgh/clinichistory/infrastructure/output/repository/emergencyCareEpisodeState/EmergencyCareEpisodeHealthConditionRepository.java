package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.emergencyCareEpisodeState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyCareEpisodeHealthConditionRepository {

	List<HealthConditionVo> findAllDiagnoses(Integer episodeId);

}
