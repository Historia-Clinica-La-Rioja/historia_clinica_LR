package net.pladema.emergencycare.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareEpisodeDischargeOtherTypeDescriptionStorage;

import net.pladema.emergencycare.infrastructure.output.entity.EmergencyCareEpisodeDischargeOtherTypeDescription;

import net.pladema.emergencycare.infrastructure.output.repository.EmergencyCareEpisodeDischargeOtherTypeDescriptionRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmergencyCareEpisodeDischargeOtherTypeDescriptionStorageImpl implements EmergencyCareEpisodeDischargeOtherTypeDescriptionStorage {

	private final EmergencyCareEpisodeDischargeOtherTypeDescriptionRepository ecedotDescriptionRepository;

	@Override
	public EmergencyCareEpisodeDischargeOtherTypeDescription save(EmergencyCareEpisodeDischargeOtherTypeDescription ecedotDescription) {
		return ecedotDescriptionRepository.save(ecedotDescription);
	}

	@Override
	public Optional<EmergencyCareEpisodeDischargeOtherTypeDescription> getByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId) {
		return Optional.of(ecedotDescriptionRepository.getById(emergencyCareEpisodeId));
	}
}
