package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationSellTypePort;

import commercialmedication.cache.domain.decodedResponse.SellTypeList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationSellTypeRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationSellType;

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
