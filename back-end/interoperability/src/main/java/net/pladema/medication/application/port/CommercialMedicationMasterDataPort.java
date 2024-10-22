package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.DatabaseUpdate;

import java.util.List;

public interface CommercialMedicationMasterDataPort {

	void saveNewMasterDataFromUpdate(List<DatabaseUpdate> databaseUpdates);

	void editMasterData(List<DatabaseUpdate> databaseUpdates);

	void deleteMasterData(List<DatabaseUpdate> databaseUpdates);

}
