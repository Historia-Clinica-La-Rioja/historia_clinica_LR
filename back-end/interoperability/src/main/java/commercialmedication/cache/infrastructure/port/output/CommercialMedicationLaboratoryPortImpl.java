package commercialmedication.cache.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import commercialmedication.cache.application.port.CommercialMedicationLaboratoryPort;

import commercialmedication.cache.domain.decodedResponse.LaboratoryList;

import commercialmedication.cache.infrastructure.repository.CommercialMedicationLaboratoryRepository;

import commercialmedication.cache.infrastructure.repository.entity.CommercialMedicationLaboratory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationLaboratoryPortImpl implements CommercialMedicationLaboratoryPort {

	private final CommercialMedicationLaboratoryRepository commercialMedicationLaboratoryRepository;

	@Override
	public void saveAll(LaboratoryList laboratories) {
		commercialMedicationLaboratoryRepository.saveAll(parseToEntityList(laboratories));
	}

	private List<CommercialMedicationLaboratory> parseToEntityList(LaboratoryList laboratories) {
		return laboratories.getLaboratories().stream().map(CommercialMedicationLaboratory::new).collect(Collectors.toList());
	}

}
