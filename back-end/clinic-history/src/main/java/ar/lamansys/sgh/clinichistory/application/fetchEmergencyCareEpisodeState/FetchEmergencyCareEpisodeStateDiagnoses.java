package ar.lamansys.sgh.clinichistory.application.fetchEmergencyCareEpisodeState;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.emergencyCareEpisodeState.EmergencyCareEpisodeHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FetchEmergencyCareEpisodeStateDiagnoses {

	private static final Logger LOG = LoggerFactory.getLogger(FetchEmergencyCareEpisodeStateDiagnoses.class);

	private final EmergencyCareEpisodeHealthConditionRepository emergencyCareEpisodeHealthConditionRepository;

	public List<DiagnosisBo> getDiagnosesGeneralState(Integer episodeId) {
		LOG.debug("Input parameters -> episodeId {}", episodeId);
		GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(getEpisodeRelatedDiagnoses(episodeId));
		List<DiagnosisBo> result = new ArrayList<>();
		HealthConditionBo mainDiagnosis = generalHealthConditionBo.getMainDiagnosis();
		if (mainDiagnosis != null)
			result.add(new DiagnosisBo(mainDiagnosis));
		result.addAll(generalHealthConditionBo.getDiagnosis());
		LOG.debug("Output -> result {}", result);
		return result;
	}

	private List<HealthConditionVo> getEpisodeRelatedDiagnoses(Integer episodeId) {
		LOG.debug("Input parameters -> episodeId {}", episodeId);
		List<HealthConditionVo> result = emergencyCareEpisodeHealthConditionRepository.findAllDiagnoses(episodeId);
		LOG.debug("Output -> result {}", result);
		return result;
	}

}
