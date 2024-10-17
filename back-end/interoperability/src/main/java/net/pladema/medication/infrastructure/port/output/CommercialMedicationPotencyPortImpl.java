package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationPotencyPort;

import net.pladema.medication.domain.decodedResponse.PotencyList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationPotencyRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationPotency;

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
