package net.pladema.medicalconsultation.shockroom.application;

import lombok.AllArgsConstructor;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FetchShockRoomSectorIdImpl implements FetchShockRoomSectorId {

	private static final Logger LOG = LoggerFactory.getLogger(FetchShockRoomSectorIdImpl.class);

	private ShockroomRepository shockroomRepository;

	@Override
	public Integer execute(Integer shockRoomId) {
		LOG.debug("Input parameters -> shockRoomId {}", shockRoomId);
		Integer result = shockroomRepository.getSectorId(shockRoomId);
		LOG.debug("Output -> result {}", result);
		return result;
	}

}
