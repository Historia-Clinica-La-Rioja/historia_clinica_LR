package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationActionPort;

import net.pladema.medication.domain.decodedResponse.ActionList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationActionRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationAction;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationActionPortImpl implements CommercialMedicationActionPort {

	private final CommercialMedicationActionRepository commercialMedicationActionRepository;

	@Override
	public void saveAll(ActionList actions) {
		commercialMedicationActionRepository.saveAll(parseToEntityList(actions));
	}

	private List<CommercialMedicationAction> parseToEntityList(ActionList actions) {
		return actions.getActions().stream().map(CommercialMedicationAction::new).collect(Collectors.toList());
	}

}
