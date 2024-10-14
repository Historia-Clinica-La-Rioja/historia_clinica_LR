package net.pladema.establishment.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtySectorDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.clinicalspecialtysector.SharedClinicalSpecialtySectorPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.ClinicalSpecialtySectorStorage;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharedClinicalServiceSectorPortImpl implements SharedClinicalSpecialtySectorPort {

	private final ClinicalSpecialtySectorStorage clinicalSpecialtySectorStorage;

	@Override
	public Optional<ClinicalSpecialtySectorDto> getClinicalSpecialtySectorById(Integer id) {
		log.debug("Input parameters getClinicalSpecialtySectorById in SharedClinicalServiceSectorPort -> clinical specialty sector id {}", id);
		Optional<ClinicalSpecialtySectorDto> result = clinicalSpecialtySectorStorage.getClinicalSpecialtySectorById(id)
				.map(cssBo -> new ClinicalSpecialtySectorDto(
						cssBo.getId(),
						cssBo.getSectorId(),
						cssBo.getClinicalSpecialtyId(),
						cssBo.getDescription())
				);
		log.debug("Output -> {}", result);
		return result;
	}
}
