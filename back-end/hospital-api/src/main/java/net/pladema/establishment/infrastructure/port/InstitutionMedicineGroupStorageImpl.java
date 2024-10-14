package net.pladema.establishment.infrastructure.port;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.InstitutionMedicineGroupStorage;

import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineGroupRepository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class InstitutionMedicineGroupStorageImpl implements InstitutionMedicineGroupStorage {

	private final InstitutionMedicineGroupRepository institutionMedicineGroupRepository;

	@Override
	public Map<String, Boolean> validateFinancingMedicinesByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids,
																				String problemSctid) {
		log.debug("Input parameters -> institutionId: {}, medicineSctids: {}, problemSctid: {}", institutionId, medicineSctids, problemSctid);
		List<String> coveredMedicines = institutionMedicineGroupRepository.validateFinancingByInstitutionAndProblem(institutionId, medicineSctids, problemSctid);
		Map<String, Boolean> result = new HashMap<>();
		medicineSctids.forEach(m -> result.put(m, coveredMedicines.contains(m)));
		log.debug("Output result -> {}", result);
		return result;
	}

}
