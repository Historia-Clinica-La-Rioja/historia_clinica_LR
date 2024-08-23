package net.pladema.emergencycare.application.port.output;

import net.pladema.establishment.domain.bed.EmergencyCareBedBo;

import java.util.List;

public interface EmergencyCareBedStorage {

	List<EmergencyCareBedBo> getAllBySectorId(Integer sectorId);
}
