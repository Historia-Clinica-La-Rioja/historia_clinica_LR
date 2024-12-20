package net.pladema.snowstorm.application.port.output;

import ar.lamansys.sgh.shared.domain.medicineGroup.MedicineGroupAuditFinancedBo;

import java.util.List;
import java.util.Map;

public interface ValidateFinancingMedicationPort {

	Map<String, MedicineGroupAuditFinancedBo> validateFinancingByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids, String problemSctid);

}
