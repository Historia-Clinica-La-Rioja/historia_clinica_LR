package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationLaboratoryPort;

import net.pladema.medication.domain.decodedResponse.LaboratoryList;

import net.pladema.medication.infrastructure.repository.CommercialMedicationLaboratoryRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationLaboratory;

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
