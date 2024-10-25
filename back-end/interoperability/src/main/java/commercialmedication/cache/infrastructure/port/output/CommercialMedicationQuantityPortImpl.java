package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationQuantityPort;

import commercialmedication.cache.domain.decodedResponse.QuantityList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationQuantityRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationQuantity;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationQuantityPortImpl implements CommercialMedicationQuantityPort {

	private final CommercialMedicationQuantityRepository commercialMedicationQuantityRepository;

	@Override
	public void saveAll(QuantityList quantities) {
		commercialMedicationQuantityRepository.saveAll(parseToEntityList(quantities));
	}

	private List<CommercialMedicationQuantity> parseToEntityList(QuantityList quantities) {
		return quantities.getQuantities().stream().map(CommercialMedicationQuantity::new).collect(Collectors.toList());
	}

}
