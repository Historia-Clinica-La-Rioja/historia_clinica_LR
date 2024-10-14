package net.pladema.establishment.infrastructure.output;

import lombok.RequiredArgsConstructor;

import net.pladema.emergencycare.application.port.output.EmergencyCareSectorStorage;

import net.pladema.emergencycare.domain.EmergencyCareAttentionPlaceBo;

import net.pladema.establishment.repository.SectorRepository;

import net.pladema.establishment.service.domain.ESectorType;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EmergencyCareSectorStorageImpl implements EmergencyCareSectorStorage {

	private final SectorRepository sectorRepository;

	@Override
	public List<EmergencyCareAttentionPlaceBo> getAllByInstitutionOrderByHierarchy(Integer institutionId) {
		return sectorRepository.findAllEmergencyCareSectorByInstitutionOrderByHierarchy(institutionId, ESectorType.GUARDIA.getId());
	}
}
