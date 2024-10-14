package net.pladema.establishment.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.application.port.ClinicalSpecialtySectorStorage;
import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;
import net.pladema.establishment.infrastructure.output.repository.ClinicalServiceSectorRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClinicalSpecialtySectorStorageImpl implements ClinicalSpecialtySectorStorage {

	private final ClinicalServiceSectorRepository clinicalServiceSectorRepository;


	@Override
	public Optional<ClinicalSpecialtySectorBo> getClinicalSpecialtySectorById(Integer id) {
		return clinicalServiceSectorRepository.findById(id)
				.map(css -> ClinicalSpecialtySectorBo.builder()
						.id(css.getId())
						.sectorId(css.getSectorId())
						.clinicalSpecialtyId(css.getClinicalSpecialtyId())
						.description(css.getDescription())
						.build());
	}

}
