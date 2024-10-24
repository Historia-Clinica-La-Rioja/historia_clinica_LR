package commercial_medication.update_schema.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationViaPort;

import commercial_medication.update_schema.cache.domain.decodedResponse.ViaList;

import commercial_medication.update_schema.cache.infrastructure.repository.CommercialMedicationViaRepository;

import commercial_medication.update_schema.cache.infrastructure.repository.entity.CommercialMedicationVia;

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
