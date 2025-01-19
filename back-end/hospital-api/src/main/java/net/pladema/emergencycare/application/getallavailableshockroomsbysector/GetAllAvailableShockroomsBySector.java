package net.pladema.emergencycare.application.getallavailableshockroomsbysector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.port.output.EmergencyCareShockroomStorage;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAllAvailableShockroomsBySector {

	private final EmergencyCareShockroomStorage emergencyCareShockroomStorage;

	public List<ShockRoomBo> run(Integer sectorId){
		log.debug("Input GetAllAvailableShockroomsBySector parameters -> sectorId {}", sectorId);
		List<ShockRoomBo> result = emergencyCareShockroomStorage.getAllAvailableBySectorId(sectorId);
		log.debug("Output -> result {}", result);
		return result;
	}
}
