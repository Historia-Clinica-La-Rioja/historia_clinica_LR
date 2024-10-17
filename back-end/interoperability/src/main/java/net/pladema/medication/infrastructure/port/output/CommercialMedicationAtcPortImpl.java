package net.pladema.medication.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.medication.application.port.CommercialMedicationAtcPort;

import net.pladema.medication.domain.decodedResponse.ATCDetailList;
import net.pladema.medication.infrastructure.repository.CommercialMedicationAtcRepository;

import net.pladema.medication.infrastructure.repository.entity.CommercialMedicationAtc;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommercialMedicationAtcPortImpl implements CommercialMedicationAtcPort {

	private final CommercialMedicationAtcRepository commercialMedicationAtcRepository;

	@Override
	public void saveAll(ATCDetailList atcs) {
		commercialMedicationAtcRepository.saveAll(parseToEntityList(atcs));
	}

	private List<CommercialMedicationAtc> parseToEntityList(ATCDetailList atcs) {
		return atcs.getAtcDetails().stream().map(CommercialMedicationAtc::new).collect(Collectors.toList());
	}

}
