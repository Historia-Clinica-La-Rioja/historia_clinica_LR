package ar.lamansys.sgh.publicapi.prescription.application.fetchprescriptionbyidanddniv3;

import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionV2Bo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchPrescriptionsByIdAndDniV3 {

	private final PrescriptionStorage prescriptionStorage;

	public PrescriptionV2Bo run(PrescriptionIdentifier prescriptionIdentifier, String identificationNumber) {
		log.debug("Input parameters -> prescriptionIdentifier {}, identificationNumber {}", prescriptionIdentifier, identificationNumber);
		PrescriptionV2Bo result = prescriptionStorage.getPrescriptionByIdAndDniV2(prescriptionIdentifier, identificationNumber)
				.orElseThrow(() -> new PrescriptionRequestException("Message", new Throwable()));
		log.debug("Output -> {}", result);
		return result;
	}
}
