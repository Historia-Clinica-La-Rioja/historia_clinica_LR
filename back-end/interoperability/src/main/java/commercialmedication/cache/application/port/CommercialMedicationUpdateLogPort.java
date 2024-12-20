package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.CommercialMedicationUpdateLogBo;

public interface CommercialMedicationUpdateLogPort {

	boolean schemaAlreadyInitialized();

	void saveNewEntry(Long logId);

	CommercialMedicationUpdateLogBo getLastNonProcessedEntry();

    void setEntryAsProcessed(Integer id, Long lastLogId);

}
