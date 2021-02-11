package net.pladema.emergencycare.triage.repository;

import net.pladema.emergencycare.triage.repository.domain.TriageVo;

import java.util.List;

public interface TriageRepositoryCustom {

    List<TriageVo> getAllByEpisodeId(Integer episodeId);

}
