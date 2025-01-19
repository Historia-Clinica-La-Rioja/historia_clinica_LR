package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationActionPort;

import commercialmedication.cache.domain.decodedResponse.ActionList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationActionRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationAction;

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
