package net.pladema.emergencycare.triage.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.triage.application.ports.TriageStorage;
import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;

import net.pladema.emergencycare.triage.infrastructure.output.repository.TriageRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TriageStorageImpl implements TriageStorage {

	private final TriageRepository triageRepository;

	@Override
	public Optional<Triage> getLatestByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId) {
		return triageRepository.findAllByEmergencyCareEpisodeIdOrderByIdDesc(emergencyCareEpisodeId)
				.stream().findFirst();
	}
}
