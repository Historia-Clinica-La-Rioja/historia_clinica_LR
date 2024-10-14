package net.pladema.establishment.application.port;

import java.util.List;
import java.util.Map;

public interface InstitutionMedicineGroupStorage {

	Map<String, Boolean> validateFinancingMedicinesByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids, String problemSctid);
}
