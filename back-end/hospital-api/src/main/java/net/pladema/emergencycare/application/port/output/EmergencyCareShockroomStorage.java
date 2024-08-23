package net.pladema.emergencycare.application.port.output;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import java.util.List;

public interface EmergencyCareShockroomStorage {

	List<ShockRoomBo> getAllBySectorId(Integer sectorId);
}
