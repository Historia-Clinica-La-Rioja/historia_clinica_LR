package snomed.relations.cache.application.ports;

import snomed.relations.cache.domain.CommercialMedicationBo;
import snomed.relations.cache.domain.GetCommercialMedicationSnomedBo;

import java.util.List;

public interface SnomedRelationCacheStorage {

	List<CommercialMedicationBo> getCommercialMedications();

	List<GetCommercialMedicationSnomedBo> getCommercialMedicationSnomedListByName(String commercialMedicationName);

}
