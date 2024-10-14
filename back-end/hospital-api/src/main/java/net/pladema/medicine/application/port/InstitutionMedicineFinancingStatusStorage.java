package net.pladema.medicine.application.port;

import java.util.List;

public interface InstitutionMedicineFinancingStatusStorage {

	void addConceptsToAllInstitutions(List<Integer> conceptsIds);
}
