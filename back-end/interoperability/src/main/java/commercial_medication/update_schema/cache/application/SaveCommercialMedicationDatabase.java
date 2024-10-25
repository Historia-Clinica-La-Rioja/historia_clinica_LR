package commercial_medication.update_schema.cache.application;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationActionPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationArticlePort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationAtcPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationControlPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationFormPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationLaboratoryPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationMonoDrugPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationNewDrugPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationPotencyPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationQuantityPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationSellTypePort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationSizePort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationUpdateFilePort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationViaPort;
import commercial_medication.update_schema.cache.application.port.CommercialMedicationSoapPort;

import commercial_medication.update_schema.cache.domain.CommercialMedicationRequestParameter;
import commercial_medication.update_schema.cache.domain.decodedResponse.CommercialMedicationDecodedResponse;

import commercial_medication.update_schema.cache.domain.decodedResponse.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveCommercialMedicationDatabase {

	private final CommercialMedicationSoapPort commercialMedicationSoapPort;

	private final CommercialMedicationActionPort commercialMedicationActionPort;

	private final CommercialMedicationArticlePort commercialMedicationArticlePort;

	private final CommercialMedicationAtcPort commercialMedicationAtcPort;

	private final CommercialMedicationControlPort commercialMedicationControlPort;

	private final CommercialMedicationFormPort commercialMedicationFormPort;

	private final CommercialMedicationLaboratoryPort commercialMedicationLaboratoryPort;

	private final CommercialMedicationMonoDrugPort commercialMedicationMonoDrugPort;

	private final CommercialMedicationNewDrugPort commercialMedicationNewDrugPort;

	private final CommercialMedicationPotencyPort commercialMedicationPotencyPort;

	private final CommercialMedicationQuantityPort commercialMedicationQuantityPort;

	private final CommercialMedicationSellTypePort commercialMedicationSellTypePort;

	private final CommercialMedicationSizePort commercialMedicationSizePort;

	private final CommercialMedicationViaPort commercialMedicationViaPort;

	private final CommercialMedicationUpdateFilePort commercialMedicationUpdateFilePort;

	@Transactional
	public void run() throws JAXBException, IOException {
		log.debug("Fetching commercial medication database...");
		CommercialMedicationRequestParameter parameters = new CommercialMedicationRequestParameter(null, null, null, CommercialMedicationRequestParameter.AFFIRMATIVE_REQUEST);
		CommercialMedicationDecodedResponse database = commercialMedicationSoapPort.callAPI(parameters);
		assertUpdateData(database);
		commercialMedicationAtcPort.saveAll(database.getAtcDetailList());

		parameters = new CommercialMedicationRequestParameter(null, CommercialMedicationRequestParameter.AFFIRMATIVE_REQUEST, CommercialMedicationRequestParameter.NEGATIVE_REQUEST, null);
		database = commercialMedicationSoapPort.callAPI(parameters);
		assertUpdateData(database);
		commercialMedicationActionPort.saveAll(database.getCommercialMedicationCompleteDatabase().getActionList());
		commercialMedicationControlPort.saveAll(database.getCommercialMedicationCompleteDatabase().getPublicSanityInternCodeList());
		commercialMedicationFormPort.saveAll(database.getCommercialMedicationCompleteDatabase().getForms());
		commercialMedicationLaboratoryPort.saveAll(database.getCommercialMedicationCompleteDatabase().getLaboratoryList());
		commercialMedicationMonoDrugPort.saveAll(database.getCommercialMedicationCompleteDatabase().getDrugList());
		commercialMedicationNewDrugPort.saveAll(database.getCommercialMedicationCompleteDatabase().getNewDrugs());
		commercialMedicationPotencyPort.saveAll(database.getCommercialMedicationCompleteDatabase().getPotencyList());
		commercialMedicationQuantityPort.saveAll(database.getCommercialMedicationCompleteDatabase().getQuantityList());
		commercialMedicationSellTypePort.saveAll(database.getCommercialMedicationCompleteDatabase().getSellTypeList());
		commercialMedicationSizePort.saveAll(database.getCommercialMedicationCompleteDatabase().getSizeList());
		commercialMedicationViaPort.saveAll(database.getCommercialMedicationCompleteDatabase().getViaList());
		commercialMedicationArticlePort.saveAll(database.getCommercialMedicationCompleteDatabase().getArticleList());

		commercialMedicationUpdateFilePort.saveNewEntry(database.getCommercialMedicationCompleteDatabase().getLastLog());
	}

	private void assertUpdateData(CommercialMedicationDecodedResponse updateData) {
		Assert.isTrue(updateData.getErrorCode().getCode().equals(ErrorCode.NO_ERROR_CODE), String.format("There's an error fetching commercial medication data. Error code: %s", updateData.getErrorCode()));
	}

}
