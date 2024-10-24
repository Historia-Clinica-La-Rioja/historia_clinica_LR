package commercial_medication.update_schema.cache.application.port;

import commercial_medication.update_schema.cache.domain.CommercialMedicationRequestParameter;
import commercial_medication.update_schema.cache.domain.decodedResponse.CommercialMedicationDecodedResponse;

import javax.xml.bind.JAXBException;

import java.io.IOException;

public interface SoapPort {

	CommercialMedicationDecodedResponse callAPI(CommercialMedicationRequestParameter parameters) throws JAXBException, IOException;

}
