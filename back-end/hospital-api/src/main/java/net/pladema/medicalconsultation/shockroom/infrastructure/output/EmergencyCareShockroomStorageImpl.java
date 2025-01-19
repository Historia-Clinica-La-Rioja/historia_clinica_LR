package net.pladema.medicalconsultation.shockroom.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareShockroomStorage;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmergencyCareShockroomStorageImpl implements EmergencyCareShockroomStorage {

	private final ShockroomRepository shockroomRepository;

	@Override
	public List<ShockRoomBo> getAllBySectorId(Integer sectorId) {
		return shockroomRepository.findAllBySectorId(sectorId, EEmergencyCareState.ATENCION.getId());
	}

	@Override
	public Optional<ShockRoomBo> getById(Integer id) {
		return shockroomRepository.findEmergencyCareShockRoomById(id, EEmergencyCareState.ATENCION.getId());
	}

	@Override
	public List<ShockRoomBo> getAllAvailableBySectorId(Integer sectorId) {
		return shockroomRepository.findAllBySectorId(sectorId, EEmergencyCareState.ATENCION.getId())
				.stream().filter(ShockRoomBo::isAvailable).collect(Collectors.toList());
	}
}
