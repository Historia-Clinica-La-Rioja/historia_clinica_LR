package net.pladema.emergencycare.infrastructure.output;

import lombok.RequiredArgsConstructor;

import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;

import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class EmergencyCareEpisodeStorageImpl implements EmergencyCareEpisodeStorage {

	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	@Override
	public Boolean episodeHasEvolutionNote(Integer episodeId) {
		return emergencyCareEpisodeRepository.episodeHasEvolutionNote(episodeId);
	}

	@Override
	public Boolean existsDischargeForEpisode(Integer episodeId) {
		return emergencyCareEpisodeRepository.isEpisodeMedicalOrAdministrativeDischarge(episodeId);
	}

	@Override
	public Boolean existsEpisodeInOffice(Integer doctorsOfficeId, Integer shockroomId) {
		return emergencyCareEpisodeRepository.existsEpisodeInOffice(doctorsOfficeId, shockroomId) > 0;
	}

}
