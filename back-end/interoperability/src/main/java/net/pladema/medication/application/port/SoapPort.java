package net.pladema.medication.application.port;

import javax.xml.bind.JAXBException;

import java.io.IOException;

public interface SoapPort {

	void fetchCommercialMedicationCompleteDataBase() throws JAXBException, IOException;

}
