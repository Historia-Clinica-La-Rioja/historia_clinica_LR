package ar.lamansys.sgh.clinichistory.application.getCommercialMedicationDosageFormUnitsByDosageFormName;

import ar.lamansys.sgh.clinichistory.application.ports.output.CommercialMedicationDosageFormUnitPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCommercialMedicationDosageFormUnitValuesByDosageFormName {

	private final CommercialMedicationDosageFormUnitPort commercialMedicationDosageFormUnitPort;

	public List<String> run(String name) {
		log.debug("Input parameter -> name {}", name);
		List<String> result = commercialMedicationDosageFormUnitPort.getAllValuesByDosageFormName(name);
		log.debug("Output -> {}", result);
		return result;
	}

}
