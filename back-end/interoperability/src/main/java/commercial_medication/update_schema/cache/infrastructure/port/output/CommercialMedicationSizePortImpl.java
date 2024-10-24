package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationSizePort;

import commercial_medication.update_schema.cache.domain.decodedResponse.SizeList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationSizeRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationSize;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationSizePortImpl implements CommercialMedicationSizePort {

	private final CommercialMedicationSizeRepository commercialMedicationSizeRepository;

	@Override
	public void saveAll(SizeList sizes) {
		commercialMedicationSizeRepository.saveAll(parseToEntityList(sizes));
	}

	private List<CommercialMedicationSize> parseToEntityList(SizeList sizes) {
		return sizes.getSizes().stream().map(CommercialMedicationSize::new).collect(Collectors.toList());
	}

}
