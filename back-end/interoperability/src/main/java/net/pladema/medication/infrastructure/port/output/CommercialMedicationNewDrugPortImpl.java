package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationNewDrugPort;

import net.pladema.medication.domain.decodedResponse.NewDrugsList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationNewDrugRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationNewDrug;

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
