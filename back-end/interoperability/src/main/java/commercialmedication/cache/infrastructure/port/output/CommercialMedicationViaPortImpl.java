package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationViaPort;

import commercialmedication.cache.domain.decodedResponse.ViaList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationViaRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationVia;

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
