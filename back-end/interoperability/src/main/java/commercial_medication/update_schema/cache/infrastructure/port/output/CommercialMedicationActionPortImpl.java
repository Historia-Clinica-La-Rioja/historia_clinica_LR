package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationActionPort;

import commercial_medication.update_schema.cache.domain.decodedResponse.ActionList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationActionRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationAction;

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
