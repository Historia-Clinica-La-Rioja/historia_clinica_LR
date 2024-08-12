package net.pladema.establishment.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareClinicalSpecialtySectorStorage;
import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;
import net.pladema.establishment.infrastructure.output.entity.ClinicalSpecialtySector;

import net.pladema.establishment.infrastructure.output.repository.ClinicalServiceSectorRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmergencyCareClinicalSpecialtySectorStorageImpl implements EmergencyCareClinicalSpecialtySectorStorage {

	private final ClinicalServiceSectorRepository clinicalServiceSectorRepository;

	@Override
	public List<ClinicalSpecialtySectorBo> getAllBySectorTypeAndInstitution(Short sectorTypeId, Integer institutionId) {
		return clinicalServiceSectorRepository.findAllBySectorTypeAndInstitution(sectorTypeId,institutionId)
				.stream().map(this::mapToBo).collect(Collectors.toList());
	}

	@Override
	public String getDescriptionById(Integer clinicalSpecialtySectorId) {
		return clinicalServiceSectorRepository.findDescriptionById(clinicalSpecialtySectorId);
	}

	private ClinicalSpecialtySectorBo mapToBo(ClinicalSpecialtySector clinicalSpecialtySector) {
		return ClinicalSpecialtySectorBo.builder()
				.id(clinicalSpecialtySector.getId())
				.sectorId(clinicalSpecialtySector.getSectorId())
				.clinicalSpecialtyId(clinicalSpecialtySector.getClinicalSpecialtyId())
				.description(clinicalSpecialtySector.getDescription())
				.build();
	}
}
