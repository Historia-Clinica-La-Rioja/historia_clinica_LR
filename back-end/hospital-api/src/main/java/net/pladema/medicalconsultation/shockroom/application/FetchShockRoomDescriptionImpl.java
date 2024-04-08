package net.pladema.medicalconsultation.shockroom.application;

import lombok.AllArgsConstructor;

import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FetchShockRoomDescriptionImpl implements FetchShockRoomDescription {

	private static final Logger LOG = LoggerFactory.getLogger(FetchShockRoomDescriptionImpl.class);

	private ShockroomRepository shockroomRepository;

	@Override
	public String execute(Integer shockRoomId) {
		LOG.debug("Input parameters -> shockRoomId {}", shockRoomId);
		String result = shockroomRepository.getDescription(shockRoomId);
		LOG.debug("Output -> result {}", result);
		return result;
	}

}
