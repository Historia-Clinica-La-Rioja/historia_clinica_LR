package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationSellTypePort;

import net.pladema.medication.domain.decodedResponse.SellTypeList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationSellTypeRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationSellType;

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
