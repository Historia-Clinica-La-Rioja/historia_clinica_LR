package net.pladema.snowstorm.application.port.output;

import java.util.List;
import java.util.Map;

public interface ValidateFinancingMedicationPort {

	Map<String, Boolean> validateFinancingByInstitutionAndProblem(Integer institutionId, List<String> medicineSctids, String problemSctid);

}
