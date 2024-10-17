package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationControlPort;

import net.pladema.medication.domain.decodedResponse.PublicSanityInternCodeList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationControlRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationControl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationControlPortImpl implements CommercialMedicationControlPort {

	private final CommercialMedicationControlRepository commercialMedicationControlRepository;

	@Override
	public void saveAll(PublicSanityInternCodeList controls) {
		commercialMedicationControlRepository.saveAll(parseToEntityList(controls));
	}

	private List<CommercialMedicationControl> parseToEntityList(PublicSanityInternCodeList controls) {
		return controls.getPublicSanityInternCodes().stream().map(CommercialMedicationControl::new).collect(Collectors.toList());
	}

}
