package net.pladema.medicalconsultation.shockroom.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareShockroomStorage;
import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmergencyCareShockroomStorageImpl implements EmergencyCareShockroomStorage {

	private final ShockroomRepository shockroomRepository;

	@Override
	public List<ShockRoomBo> getAllBySectorId(Integer sectorId) {
		return shockroomRepository.findAllBySectorId(sectorId);
	}
}
