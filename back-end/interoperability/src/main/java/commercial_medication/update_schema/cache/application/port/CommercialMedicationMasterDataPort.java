package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.decodedResponse.DatabaseUpdate;

import java.util.List;

public interface CommercialMedicationMasterDataPort {

	void saveNewMasterDataFromUpdate(List<DatabaseUpdate> databaseUpdates);

	void editMasterData(List<DatabaseUpdate> databaseUpdates);

	void deleteMasterData(List<DatabaseUpdate> databaseUpdates);

}
