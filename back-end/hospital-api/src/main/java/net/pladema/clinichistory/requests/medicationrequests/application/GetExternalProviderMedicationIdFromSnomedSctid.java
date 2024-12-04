package net.pladema.clinichistory.requests.medicationrequests.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.medicationrequests.application.port.output.CommercialMedicationSchemaPort;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetExternalProviderMedicationIdFromSnomedSctid {

	private final CommercialMedicationSchemaPort commercialMedicationSchemaPort;

	public Integer run(String snomedSctid) {
		log.debug("Input parameters -> snomedSctid {}", snomedSctid);
		Integer result = commercialMedicationSchemaPort.getExternalProviderMedicationIdFromSnomedSctid(snomedSctid);
		log.debug("Output -> {}", result);
		return result;
	}

}
