package net.pladema.medication.application;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.medication.application.port.CommercialMedicationActionPort;
import net.pladema.medication.application.port.CommercialMedicationArticlePort;
import net.pladema.medication.application.port.CommercialMedicationAtcPort;
import net.pladema.medication.application.port.CommercialMedicationControlPort;
import net.pladema.medication.application.port.CommercialMedicationFormPort;
import net.pladema.medication.application.port.CommercialMedicationLaboratoryPort;
import net.pladema.medication.application.port.CommercialMedicationMonoDrugPort;
import net.pladema.medication.application.port.CommercialMedicationNewDrugPort;
import net.pladema.medication.application.port.CommercialMedicationPotencyPort;
import net.pladema.medication.application.port.CommercialMedicationQuantityPort;
import net.pladema.medication.application.port.CommercialMedicationSellTypePort;
import net.pladema.medication.application.port.CommercialMedicationSizePort;
import net.pladema.medication.application.port.CommercialMedicationUpdateFilePort;
import net.pladema.medication.application.port.CommercialMedicationViaPort;
import net.pladema.medication.application.port.SoapPort;

import net.pladema.medication.domain.CommercialMedicationRequestParameter;
import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;

import net.pladema.medication.domain.decodedResponse.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveCommercialMedicationDatabase {

	private final SoapPort soapPort;

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
		CommercialMedicationRequestParameter parameters = new CommercialMedicationRequestParameter(null, null, null, CommercialMedicationRequestParameter.AFFIRMATIVE_REQUEST, false);
		CommercialMedicationDecodedResponse database = soapPort.callAPIWithNoFile(parameters);
		assertUpdateData(database);
		commercialMedicationAtcPort.saveAll(database.getAtcDetailList());

		parameters = new CommercialMedicationRequestParameter(null, CommercialMedicationRequestParameter.AFFIRMATIVE_REQUEST, CommercialMedicationRequestParameter.NEGATIVE_REQUEST, null, false);
		database = soapPort.callAPIWithNoFile(parameters);
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
