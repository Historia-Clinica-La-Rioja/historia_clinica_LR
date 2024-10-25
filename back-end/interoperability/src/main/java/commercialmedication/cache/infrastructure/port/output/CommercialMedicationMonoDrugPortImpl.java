package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationMonoDrugPort;

import commercialmedication.cache.domain.decodedResponse.DrugList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationMonoDrugRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationMonoDrug;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationMonoDrugPortImpl implements CommercialMedicationMonoDrugPort {

	private final CommercialMedicationMonoDrugRepository commercialMedicationMonoDrugRepository;

	@Override
	public void saveAll(DrugList drugs) {
		commercialMedicationMonoDrugRepository.saveAll(parseToEntityList(drugs));
	}

	private List<CommercialMedicationMonoDrug> parseToEntityList(DrugList drugs) {
		return drugs.getDrugs().stream().map(CommercialMedicationMonoDrug::new).collect(Collectors.toList());
	}

}
