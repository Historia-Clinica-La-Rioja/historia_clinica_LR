package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationControlPort;

import commercialmedication.cache.domain.decodedResponse.PublicSanityInternCodeList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationControlRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationControl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationControlPortImpl implements CommercialMedicationControlPort {

	private final CommercialMedicationControlRepository commercialMedicationControlRepository;

	@Override
	public void saveAll(PublicSanityInternCodeList controls) {
		commercialMedicationControlRepository.saveAll(parseToEntityList(controls));
	}

	private List<CommercialMedicationControl> parseToEntityList(PublicSanityInternCodeList controls) {
		return controls.getPublicSanityInternCodes().stream().map(CommercialMedicationControl::new).collect(Collectors.toList());
	}

}
