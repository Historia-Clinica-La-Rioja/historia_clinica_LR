package net.pladema.medicalconsultation.shockroom.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	public static final String OUTPUT = "Output -> {}";

	@Override
	public List<ShockRoomBo> getShockrooms(Integer instutitionId) {
		log.debug("Input parameters -> institutionId {}", instutitionId);
		List<ShockRoomBo> result = shockroomRepository.getShockrooms(instutitionId)
				.stream()
				.map(entity -> new ShockRoomBo(entity.getId(), entity.getDescription()))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}
}
