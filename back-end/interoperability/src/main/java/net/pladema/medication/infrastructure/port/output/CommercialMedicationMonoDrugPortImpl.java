package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationMonoDrugPort;

import net.pladema.medication.domain.decodedResponse.DrugList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationMonoDrugRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationMonoDrug;

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
