package net.pladema.emergencycare.application.port.output;

import net.pladema.establishment.domain.bed.EmergencyCareBedBo;

import java.util.List;
import java.util.Optional;

public interface EmergencyCareBedStorage {

	List<EmergencyCareBedBo> getAllBySectorId(Integer sectorId);
	Optional<EmergencyCareBedBo> getById(Integer id);
}
