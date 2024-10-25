package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.CommercialMedicationFileUpdateBo;

public interface CommercialMedicationUpdateFilePort {

	boolean schemaAlreadyInitialized();

	void saveNewEntry(Long logId);

	CommercialMedicationFileUpdateBo getLastNonProcessedEntry();

    void setEntryAsProcessed(Integer id, Long lastLogId);

}
