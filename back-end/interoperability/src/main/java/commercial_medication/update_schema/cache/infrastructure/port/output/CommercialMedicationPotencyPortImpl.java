package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationPotencyPort;

import commercial_medication.update_schema.cache.domain.decodedResponse.PotencyList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationPotencyRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationPotency;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationPotencyPortImpl implements CommercialMedicationPotencyPort {

	private final CommercialMedicationPotencyRepository commercialMedicationPotencyRepository;

	@Override
	public void saveAll(PotencyList potencies) {
		commercialMedicationPotencyRepository.saveAll(parseToEntityList(potencies));
	}

	private List<CommercialMedicationPotency> parseToEntityList(PotencyList potencies) {
		return potencies.getPotencies().stream().map(CommercialMedicationPotency::new).collect(Collectors.toList());
	}

}
