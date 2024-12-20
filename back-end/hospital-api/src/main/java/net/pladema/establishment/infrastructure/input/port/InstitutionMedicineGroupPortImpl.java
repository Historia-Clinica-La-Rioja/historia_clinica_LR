package net.pladema.establishment.infrastructure.input.port;

import ar.lamansys.sgh.shared.domain.medicineGroup.MedicineGroupAuditFinancedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.medicinegroup.ValidateFinancingMedicinesByInstitutionAndProblem;

import net.pladema.snowstorm.application.port.output.ValidateFinancingMedicationPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class InstitutionMedicineGroupPortImpl implements ValidateFinancingMedicationPort {

	private final ValidateFinancingMedicinesByInstitutionAndProblem validateFinancingMedicinesByInstitutionAndProblem;

	@Override
	public Map<String, MedicineGroupAuditFinancedBo> validateFinancingByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids, String problemSctid) {
		log.debug("Input parameters -> institutionId: {}, medicineSctids: {}, problemSctid: {}", institutionId, medicineSctids, problemSctid);
		return validateFinancingMedicinesByInstitutionAndProblem.run(institutionId, medicineSctids, problemSctid);
	}

}
