package net.pladema.medicalconsultation.shockroom.application;

import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

import java.util.List;

public interface ShockroomStorage {

	List<ShockRoomBo> getShockrooms(Integer instutitionId);
}
