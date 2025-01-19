package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationNewDrugPort;

import commercialmedication.cache.domain.decodedResponse.NewDrugsList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationNewDrugRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationNewDrug;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationNewDrugPortImpl implements CommercialMedicationNewDrugPort {

	private final CommercialMedicationNewDrugRepository commercialMedicationNewDrugRepository;

	@Override
	public void saveAll(NewDrugsList newDrugs) {
		commercialMedicationNewDrugRepository.saveAll(parseToEntityList(newDrugs));
	}

	private List<CommercialMedicationNewDrug> parseToEntityList(NewDrugsList newDrugs) {
		return newDrugs.getNewDrugs().stream().map(CommercialMedicationNewDrug::new).collect(Collectors.toList());
	}

}
