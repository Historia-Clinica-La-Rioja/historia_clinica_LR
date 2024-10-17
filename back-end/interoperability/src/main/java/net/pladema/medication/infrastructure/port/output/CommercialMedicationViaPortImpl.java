package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationViaPort;

import net.pladema.medication.domain.decodedResponse.ViaList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationViaRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationVia;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationViaPortImpl implements CommercialMedicationViaPort {

	private final CommercialMedicationViaRepository commercialMedicationViaRepository;

	@Override
	public void saveAll(ViaList vias) {
		commercialMedicationViaRepository.saveAll(parseToEntityList(vias));
	}

	private List<CommercialMedicationVia> parseToEntityList(ViaList vias) {
		return vias.getVias().stream().map(CommercialMedicationVia::new).collect(Collectors.toList());
	}

}
