package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationFormPort;

import commercial_medication.update_schema.cache.domain.decodedResponse.FormList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationFormRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationForm;

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
