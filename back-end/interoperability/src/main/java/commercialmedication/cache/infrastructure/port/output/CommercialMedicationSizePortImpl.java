package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationSizePort;

import commercialmedication.cache.domain.decodedResponse.SizeList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationSizeRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationSize;

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
