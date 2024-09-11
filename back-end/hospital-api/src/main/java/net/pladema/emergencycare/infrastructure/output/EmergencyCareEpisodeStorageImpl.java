package net.pladema.emergencycare.infrastructure.output;

import lombok.RequiredArgsConstructor;

import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeStorage;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;

import net.pladema.emergencycare.service.domain.EmergencyCareBo;

import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.stereotype.Service;

import java.util.Optional;


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

	@Override
	public Optional<EmergencyCareBo> getByBedIdInAttention(Integer bedId) {
		return emergencyCareEpisodeRepository.findByBedIdInAttention(bedId).map(EmergencyCareBo::new);
	}

	@Override
	public ProfessionalPersonBo getProfessionalByEpisodeId(Integer id) {
		return new ProfessionalPersonBo(emergencyCareEpisodeRepository.getEmergencyCareEpisodeRelatedProfessionalInfo(id));
	}

	@Override
	public Optional<EmergencyCareBo> getByShockroomIdInAttention(Integer shockroomId) {
		return emergencyCareEpisodeRepository.findByShockroomIdInAttention(shockroomId).map(EmergencyCareBo::new);
	}

}
