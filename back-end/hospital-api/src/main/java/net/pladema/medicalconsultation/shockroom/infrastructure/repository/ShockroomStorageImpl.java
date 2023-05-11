package net.pladema.medicalconsultation.shockroom.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.shockroom.application.ShockroomStorage;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShockroomStorageImpl implements ShockroomStorage {

	private final ShockroomRepository shockroomRepository;
	private final EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;
	public static final String OUTPUT = "Output -> {}";

	@Override
	public List<ShockRoomBo> getShockrooms(Integer instutitionId) {
		log.debug("Input parameters -> institutionId {}", instutitionId);
		List<ShockRoomBo> result = shockroomRepository.getShockrooms(instutitionId)
				.stream()
				.map(entity -> {
					boolean isAvailable = emergencyCareEpisodeRepository.existsEpisodeInOffice(null, entity.getId()) == 0;
					ShockRoomBo shockRoomBo = new ShockRoomBo(entity.getId(), entity.getDescription());
					shockRoomBo.setAvailable(isAvailable);
					return shockRoomBo;
				})
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}
}
