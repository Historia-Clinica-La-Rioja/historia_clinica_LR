package ar.lamansys.sgh.publicapi.application.fetchprescriptionsdatabydni;

import ar.lamansys.sgh.publicapi.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.application.port.out.PrescriptionStorage;

import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionBo;
import ar.lamansys.sgh.publicapi.domain.prescription.PrescriptionsDataBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FetchPrescriptionsDataByDni {

	private final Logger logger;
	private final PrescriptionStorage prescriptionStorage;

	public FetchPrescriptionsDataByDni(PrescriptionStorage prescriptionStorage) {
		this.prescriptionStorage = prescriptionStorage;
		this.logger = LoggerFactory.getLogger(FetchPrescriptionsByIdAndDni.class);
	}

	public List<PrescriptionsDataBo> run(String identificationNumber) {
		logger.debug("Input parameters ->  identificationNumber {}", identificationNumber);
		List<PrescriptionsDataBo> result = getFromStorage(identificationNumber);
		logger.debug("Output -> {}", result);
		return result;
	}

	private List<PrescriptionsDataBo> getFromStorage(String identificationNumber) {
		return prescriptionStorage.getPrescriptionsDataByDni(identificationNumber).orElse(Collections.emptyList());
	}
}
