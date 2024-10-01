package net.pladema.establishment.application.port;

import ar.lamansys.sgh.shared.domain.medicineGroup.MedicineGroupAuditFinancedBo;

import java.util.List;
import java.util.Map;

public interface InstitutionMedicineGroupStorage {

	Map<String, MedicineGroupAuditFinancedBo> validateFinancingMedicinesByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids, String problemSctid);
}
