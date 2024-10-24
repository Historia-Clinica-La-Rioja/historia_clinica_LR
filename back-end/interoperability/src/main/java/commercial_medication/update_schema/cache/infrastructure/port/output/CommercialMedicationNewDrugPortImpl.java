package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationNewDrugPort;

import commercial_medication.update_schema.cache.domain.decodedResponse.NewDrugsList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationNewDrugRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationNewDrug;

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
