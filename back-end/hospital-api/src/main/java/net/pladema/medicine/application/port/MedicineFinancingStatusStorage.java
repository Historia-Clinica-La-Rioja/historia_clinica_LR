package net.pladema.medicine.application.port;

import java.util.List;

public interface MedicineFinancingStatusStorage {

	List<Integer> addConcepts(List<Integer> conceptIds);

}
