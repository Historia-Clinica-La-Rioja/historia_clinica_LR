package net.pladema.emergencycare.application.port.output;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import java.util.List;
import java.util.Optional;

public interface EmergencyCareShockroomStorage {

	List<ShockRoomBo> getAllBySectorId(Integer sectorId);
	Optional<ShockRoomBo> getById(Integer id);
}
