package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationAtcPort;

import commercialmedication.cache.domain.decodedResponse.ATCDetailList;
import commercialmedication.cache.infrastructure.repository.CommercialMedicationAtcRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationAtc;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationAtcPortImpl implements CommercialMedicationAtcPort {

	private final CommercialMedicationAtcRepository commercialMedicationAtcRepository;

	@Override
	public void saveAll(ATCDetailList atcs) {
		commercialMedicationAtcRepository.saveAll(parseToEntityList(atcs));
	}

	private List<CommercialMedicationAtc> parseToEntityList(ATCDetailList atcs) {
		return atcs.getAtcDetails().stream().map(CommercialMedicationAtc::new).collect(Collectors.toList());
	}

}
