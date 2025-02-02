package net.pladema.establishment.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareBedStorage;
import net.pladema.establishment.domain.bed.EmergencyCareBedBo;

import net.pladema.establishment.repository.BedRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmergencyCareBedStorageImpl implements EmergencyCareBedStorage {

	private final BedRepository bedRepository;

	@Override
	public List<EmergencyCareBedBo> getAllBySectorId(Integer sectorId) {
		return bedRepository.findAllEmergencyCareBedBySectorId(sectorId);
	}

	@Override
	public Optional<EmergencyCareBedBo> getById(Integer id) {
		return bedRepository.findEmergencyCareBedById(id);
	}
}
