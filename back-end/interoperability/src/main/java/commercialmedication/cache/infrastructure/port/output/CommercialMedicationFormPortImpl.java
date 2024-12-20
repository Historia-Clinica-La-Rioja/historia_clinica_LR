package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationFormPort;

import commercialmedication.cache.domain.decodedResponse.FormList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationFormRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationForm;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationFormPortImpl implements CommercialMedicationFormPort {

	private final CommercialMedicationFormRepository commercialMedicationFormRepository;

	@Override
	public void saveAll(FormList forms) {
		commercialMedicationFormRepository.saveAll(parseToEntityList(forms));
	}

	private List<CommercialMedicationForm> parseToEntityList(FormList forms) {
		return forms.getForms().stream().map(CommercialMedicationForm::new).collect(Collectors.toList());
	}

}
