package net.pladema.medication.application;

import lombok.RequiredArgsConstructor;

import net.pladema.medication.application.port.CommercialMedicationArticlePort;
import net.pladema.medication.application.port.CommercialMedicationMasterDataPort;
import net.pladema.medication.application.port.CommercialMedicationUpdateFilePort;

import net.pladema.medication.application.port.SoapPort;

import net.pladema.medication.domain.CommercialMedicationFileUpdateBo;
import net.pladema.medication.domain.CommercialMedicationRequestParameter;

import net.pladema.medication.domain.CommercialMedicationResponse;

import net.pladema.medication.domain.decodedResponse.CommercialMedicationDatabaseUpdate;

import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;
import net.pladema.medication.domain.decodedResponse.DatabaseUpdate;

import net.pladema.medication.domain.decodedResponse.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UpdateCommercialMedicationSchema {

	private final String PRICE_UPDATE_OPERATION_CODE = "P";

	private final String NEW_ARTICLE_OPERATION_CODE = "A";

	private final String EDITED_ARTICLE_OPERATION_CODE = "M";

	private final String RE_ENABLED_ARTICLE_OPERATION_CODE = "R";

	private final String DISABLE_ARTICLE_OPERATION_CODE = "B";

	private final String NEW_MASTER_DATA_OPERATION_CODE = "T";

	private final String EDITED_MASTER_DATA_OPERATION_CODE = "C";

	private final String DISABLE_MASTER_DATA_OPERATION_CODE = "D";

	private final SoapPort soapPort;

	private final CommercialMedicationUpdateFilePort commercialMedicationUpdateFilePort;

	private final CommercialMedicationArticlePort commercialMedicationArticlePort;

	private final CommercialMedicationMasterDataPort commercialMedicationMasterDataPort;

	public void run() throws JAXBException, IOException {
		CommercialMedicationFileUpdateBo lastEntry = commercialMedicationUpdateFilePort.getLastNonProcessedEntry();
		if (lastEntry.getFilePath() != null)
			handleOldUpdate(lastEntry);
		handleNewUpdate(lastEntry.getLogId());
	}

	private void handleOldUpdate(CommercialMedicationFileUpdateBo lastEntry) throws IOException, JAXBException {
		CommercialMedicationDecodedResponse oldUpdateData = commercialMedicationUpdateFilePort.getOldUpdateFile(lastEntry.getFilePath());
		assertUpdateData(oldUpdateData);
		Long lastLogId = updateCommercialMedications(oldUpdateData.getCommercialMedicationDatabaseUpdate());
		commercialMedicationUpdateFilePort.setEntryAsProcessed(lastEntry.getLogId(), lastLogId);
		lastEntry.setLogId(lastLogId);
	}

	private void handleNewUpdate(Long logId) throws JAXBException, IOException {
		CommercialMedicationRequestParameter parameters = new CommercialMedicationRequestParameter(logId, null, null, null, true);
		CommercialMedicationResponse updateData = soapPort.callAPIWithFile(parameters);
		assertUpdateData(updateData.getCommercialMedicationDecodedResponse());
		commercialMedicationUpdateFilePort.updateEntryFilePath(logId, updateData.getFilePath());
		Long lastLogId = updateCommercialMedications(updateData.getCommercialMedicationDecodedResponse().getCommercialMedicationDatabaseUpdate());
		commercialMedicationUpdateFilePort.setEntryAsProcessed(logId, lastLogId);
	}

	private void assertUpdateData(CommercialMedicationDecodedResponse updateData) {
		Assert.isTrue(updateData.getErrorCode().getCode().equals(ErrorCode.NO_ERROR_CODE), String.format("There's an error fetching commercial medication data. Error code: %s", updateData.getErrorCode()));
	}

	private Long updateCommercialMedications(CommercialMedicationDatabaseUpdate updateData) {
		List<DatabaseUpdate> toProcess = new ArrayList<>();
		int index = 0;
		while (index < updateData.getDatabaseUpdates().size()) {
			toProcess.add(updateData.getDatabaseUpdates().get(index));
			index++;
			while (index < updateData.getDatabaseUpdates().size() && updateData.getDatabaseUpdates().get(index).getOperationType().equals(toProcess.get(0).getOperationType())) {
				toProcess.add(updateData.getDatabaseUpdates().get(index));
				index++;
			}
			processUpdates(toProcess, toProcess.get(0).getOperationType());
			if (index < updateData.getDatabaseUpdates().size())
				toProcess.clear();
		}
		return toProcess.get(toProcess.size() - 1).getLogId();
	}

	private void processUpdates(List<DatabaseUpdate> updates, String operationCode) {
		switch (operationCode) {
			case NEW_ARTICLE_OPERATION_CODE:
				commercialMedicationArticlePort.saveAllNewArticlesFromUpdate(updates);
				break;
			case EDITED_ARTICLE_OPERATION_CODE:
				commercialMedicationArticlePort.editArticles(updates);
				break;
			case RE_ENABLED_ARTICLE_OPERATION_CODE:
				commercialMedicationArticlePort.reEnableAll(updates);
				break;
			case PRICE_UPDATE_OPERATION_CODE:
				commercialMedicationArticlePort.updatePrices(updates);
				break;
			case DISABLE_ARTICLE_OPERATION_CODE:
				commercialMedicationArticlePort.deleteAll(updates);
				break;
			case NEW_MASTER_DATA_OPERATION_CODE:
				commercialMedicationMasterDataPort.saveNewMasterDataFromUpdate(updates);
				break;
			case EDITED_MASTER_DATA_OPERATION_CODE:
				commercialMedicationMasterDataPort.editMasterData(updates);
				break;
			case DISABLE_MASTER_DATA_OPERATION_CODE:
				commercialMedicationMasterDataPort.deleteMasterData(updates);
				break;
		}
	}

}
