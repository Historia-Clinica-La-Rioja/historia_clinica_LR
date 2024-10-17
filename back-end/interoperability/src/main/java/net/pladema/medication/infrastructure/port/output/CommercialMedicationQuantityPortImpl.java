package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationQuantityPort;

import net.pladema.medication.domain.decodedResponse.QuantityList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationQuantityRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationQuantity;

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
