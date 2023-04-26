package net.pladema.medicalconsultation.shockroom.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FetchShockroomsImpl implements FetchShockrooms {

	private final ShockroomStorage shockroomStorage;
	public static final String OUTPUT = "Output -> {}";

	@Override
	public List<ShockRoomBo> execute(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<ShockRoomBo> result = shockroomStorage.getShockrooms(institutionId);
		log.debug(OUTPUT, result);
		return result;
	}
}
