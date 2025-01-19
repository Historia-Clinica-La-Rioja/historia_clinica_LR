package commercialmedication.cache.application.port;

import commercialmedication.cache.domain.CommercialMedicationRequestParameter;
import commercialmedication.cache.domain.decodedResponse.CommercialMedicationDecodedResponse;

import javax.xml.bind.JAXBException;

import java.io.IOException;

public interface CommercialMedicationSoapPort {

	CommercialMedicationDecodedResponse callAPI(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException;

}
