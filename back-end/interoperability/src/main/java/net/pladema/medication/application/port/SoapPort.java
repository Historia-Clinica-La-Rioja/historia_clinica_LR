package net.pladema.medication.application.port;

import net.pladema.medication.domain.CommercialMedicationRequestParameter;
import net.pladema.medication.domain.CommercialMedicationResponse;
import net.pladema.medication.domain.decodedResponse.CommercialMedicationDecodedResponse;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;

public interface SoapPort {

	CommercialMedicationDecodedResponse callAPIWithNoFile(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException;

	CommercialMedicationResponse callAPIWithFile(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException;

	CommercialMedicationDecodedResponse unmarshallCommercialMedicationDecodedResponseXml(InputStream fileContent) throws JAXBException;
}
