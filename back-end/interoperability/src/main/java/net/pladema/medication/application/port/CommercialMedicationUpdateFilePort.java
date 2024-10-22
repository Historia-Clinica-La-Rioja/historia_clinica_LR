package net.pladema.medication.application.port;

import net.pladema.medication.domain.CommercialMedicationFileUpdateBo;
import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;

import javax.xml.bind.JAXBException;

import java.io.IOException;

public interface CommercialMedicationUpdateFilePort {

	boolean schemaAlreadyInitialized();

	void saveNewEntry(Long logId);

	void updateEntryFilePath(Long logId, String filePath);

	CommercialMedicationFileUpdateBo getLastNonProcessedEntry();

    void setEntryAsProcessed(Long oldLogId, Long lastLogId);

	CommercialMedicationDecodedResponse getOldUpdateFile(String filePath) throws IOException, JAXBException;

}
