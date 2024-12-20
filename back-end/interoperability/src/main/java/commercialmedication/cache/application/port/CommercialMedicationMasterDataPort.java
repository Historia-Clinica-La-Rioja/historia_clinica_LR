package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.decodedResponse.DatabaseUpdate;

import java.util.List;

public interface CommercialMedicationMasterDataPort {

	void saveNewMasterDataFromUpdate(List<DatabaseUpdate> databaseUpdates);

	void editMasterData(List<DatabaseUpdate> databaseUpdates);

	void deleteMasterData(List<DatabaseUpdate> databaseUpdates);

}
