package net.pladema.establishment.application.medicinegroup;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.port.InstitutionMedicineGroupStorage;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class ValidateFinancingMedicinesByInstitutionAndProblem {

	InstitutionMedicineGroupStorage institutionMedicineGroupStorage;

	public Map<String, Boolean> run(Integer institutionId, List<String> medicineSctids, String problemSctid) {
		log.debug("Input parameters -> institutionId: {}, medicineSctids: {}, problemSctid: {}", institutionId, medicineSctids, problemSctid);
		Map<String, Boolean> result = institutionMedicineGroupStorage.validateFinancingMedicinesByInstitutionAndProblem(institutionId, medicineSctids, problemSctid);
		log.debug("Output result -> {}", result.size());
		return result;
	}

}
