package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationQuantityPort;

import commercial_medication.update_schema.cache.domain.decodedResponse.QuantityList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationQuantityRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationQuantity;

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
