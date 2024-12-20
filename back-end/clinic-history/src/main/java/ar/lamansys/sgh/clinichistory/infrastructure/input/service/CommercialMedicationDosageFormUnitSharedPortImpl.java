package ar.lamansys.sgh.clinichistory.infrastructure.input.service;

import ar.lamansys.sgh.clinichistory.application.getCommercialMedicationDosageFormUnitsByDosageFormName.GetCommercialMedicationDosageFormUnitValuesByDosageFormName;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicationstatement.CommercialMedicationDosageFormUnitSharedPort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommercialMedicationDosageFormUnitSharedPortImpl implements CommercialMedicationDosageFormUnitSharedPort {

	private final GetCommercialMedicationDosageFormUnitValuesByDosageFormName getCommercialMedicationDosageFormUnitValuesByDosageFormName;

	@Override
	public List<String> getAllValuesByDosageFormName(String name) {
		log.debug("Input parameter -> name {}", name);
		List<String> result = getCommercialMedicationDosageFormUnitValuesByDosageFormName.run(name);
		log.debug("Output -> {}", result);
		return result;
	}

}
