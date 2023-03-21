package ar.lamansys.sgh.publicapi.application.fetchprescriptionsbyidanddni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;

@Service
public class FetchPrescriptionsByIdAndDni {

	private final Logger logger;
	private final PrescriptionStorage prescriptionStorage;


	public FetchPrescriptionsByIdAndDni(PrescriptionStorage prescriptionStorage) {
		this.prescriptionStorage = prescriptionStorage;
		this.logger = LoggerFactory.getLogger(FetchPrescriptionsByIdAndDni.class);
	}

	public PrescriptionBo run(String prescriptionId, String identificationNumber) {
		logger.debug("Input parameters -> prescriptionId {}, identificationNumber {}", prescriptionId, identificationNumber);
		PrescriptionBo result = getFromStorage(prescriptionId, identificationNumber);
		logger.debug("Output -> {}", result);
		return result;
	}

	private PrescriptionBo getFromStorage(String prescriptionId, String identificationNumber) {
		return prescriptionStorage.getPrescriptionByIdAndDni(prescriptionId, identificationNumber).orElse(new PrescriptionBo());
	}
}
