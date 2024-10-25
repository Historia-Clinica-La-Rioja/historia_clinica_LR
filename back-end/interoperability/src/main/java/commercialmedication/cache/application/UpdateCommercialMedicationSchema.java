package commercialmedication.cache.application;

import lombok.RequiredArgsConstructor;

import commercialmedication.cache.application.port.CommercialMedicationArticlePort;
import commercialmedication.cache.application.port.CommercialMedicationMasterDataPort;
import commercialmedication.cache.application.port.CommercialMedicationUpdateFilePort;

import commercialmedication.cache.application.port.CommercialMedicationSoapPort;

import commercialmedication.cache.domain.CommercialMedicationFileUpdateBo;
import commercialmedication.cache.domain.CommercialMedicationRequestParameter;

import commercialmedication.cache.domain.decodedResponse.CommercialMedicationDatabaseUpdate;

import commercialmedication.cache.domain.decodedResponse.CommercialMedicationDecodedResponse;
import commercialmedication.cache.domain.decodedResponse.DatabaseUpdate;

import commercialmedication.cache.domain.decodedResponse.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	private final CommercialMedicationSoapPort commercialMedicationSoapPort;

	private final CommercialMedicationUpdateFilePort commercialMedicationUpdateFilePort;

	private final CommercialMedicationArticlePort commercialMedicationArticlePort;

	private final CommercialMedicationMasterDataPort commercialMedicationMasterDataPort;

	@Transactional
	public void run() throws JAXBException, IOException {
		CommercialMedicationFileUpdateBo lastEntry = commercialMedicationUpdateFilePort.getLastNonProcessedEntry();
		CommercialMedicationRequestParameter parameters = new CommercialMedicationRequestParameter(lastEntry.getLogId(), null, null, null);
		CommercialMedicationDecodedResponse updateData = commercialMedicationSoapPort.callAPI(parameters);
		assertUpdateData(updateData);
		Long lastLogId = updateCommercialMedications(updateData.getCommercialMedicationDatabaseUpdate());
		commercialMedicationUpdateFilePort.setEntryAsProcessed(lastEntry.getId(), lastLogId);
	}

	private void assertUpdateData(CommercialMedicationDecodedResponse updateData) {
		Assert.isTrue(updateData.getErrorCode().getCode().equals(ErrorCode.NO_ERROR_CODE), String.format("There's an error fetching commercial medication data. Error code: %s", updateData.getErrorCode()));
	}

	private Long updateCommercialMedications(CommercialMedicationDatabaseUpdate updateData) {
		List<DatabaseUpdate> toProcess = new ArrayList<>();
		int index = 0;
		while (index < updateData.getDatabaseUpdates().size()) {
			do {
				toProcess.add(updateData.getDatabaseUpdates().get(index));
				index++;
			} while (index < updateData.getDatabaseUpdates().size() && updateData.getDatabaseUpdates().get(index).getOperationType().equals(toProcess.get(0).getOperationType()));
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
			case RE_ENABLED_ARTICLE_OPERATION_CODE:
				commercialMedicationArticlePort.editArticles(updates);
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
