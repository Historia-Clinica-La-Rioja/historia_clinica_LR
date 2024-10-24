package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationSellTypePort;

import commercial_medication.update_schema.cache.domain.decodedResponse.SellTypeList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationSellTypeRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationSellType;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationSellTypePortImpl implements CommercialMedicationSellTypePort {

	private final CommercialMedicationSellTypeRepository commercialMedicationSellTypeRepository;

	@Override
	public void saveAll(SellTypeList sellTypes) {
		commercialMedicationSellTypeRepository.saveAll(parseToEntityList(sellTypes));
	}

	private List<CommercialMedicationSellType> parseToEntityList(SellTypeList sellTypes) {
		return sellTypes.getSellTypes().stream().map(CommercialMedicationSellType::new).collect(Collectors.toList());
	}

}
