package snomed.relations.cache.application.ports;

import snomed.relations.cache.domain.CommercialMedicationBo;

import java.util.List;

public interface SnomedRelationCacheStorage {

	List<CommercialMedicationBo> getCommercialMedications();

}
