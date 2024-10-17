package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationSizePort;

import net.pladema.medication.domain.decodedResponse.SizeList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationSizeRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationSize;

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
