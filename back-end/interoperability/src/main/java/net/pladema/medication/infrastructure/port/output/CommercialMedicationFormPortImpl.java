package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationFormPort;

import net.pladema.medication.domain.decodedResponse.FormList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationFormRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationForm;

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
