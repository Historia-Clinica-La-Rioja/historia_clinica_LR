package net.pladema.establishment.service.impl;

import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.service.RoomService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

	private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);

	private RoomRepository roomRepository;

	@Override
	public String getRoomNumber(Integer roomId) {
		LOG.debug("Input parameter -> roomId {}", roomId);
		String result = roomRepository.getRoomNumber(roomId);
		LOG.debug("Output -> result {}", result);
		return result;
	}

	@Override
	public Integer getSectorId(Integer roomId) {
		LOG.debug("Input parameter -> roomId {}", roomId);
		Integer result = roomRepository.getSectorId(roomId);
		LOG.debug("Output -> result {}", result);
		return result;
	}

}
