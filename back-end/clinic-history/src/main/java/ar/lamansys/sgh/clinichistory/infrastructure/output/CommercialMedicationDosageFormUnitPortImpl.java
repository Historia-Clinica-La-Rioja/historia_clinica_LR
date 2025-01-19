package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.ports.output.CommercialMedicationDosageFormUnitPort;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.CommercialMedicationDosageFormUnitRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommercialMedicationDosageFormUnitPortImpl implements CommercialMedicationDosageFormUnitPort {

	private final CommercialMedicationDosageFormUnitRepository commercialMedicationDosageFormUnitRepository;

	@Override
	public List<String> getAllValuesByDosageFormName(String name) {
		return commercialMedicationDosageFormUnitRepository.fetchAllValuesByDosageFormName(name);
	}

}
