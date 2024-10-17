package net.pladema.medication.application.port;

import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;

import javax.xml.bind.JAXBException;

import java.io.IOException;

public interface SoapPort {

	CommercialMedicationDecodedResponse fetchCommercialMedicationCompleteDataBase() throws JAXBException, IOException;

	CommercialMedicationDecodedResponse fetchCommercialMedicationAtcData() throws JAXBException, IOException;

}
