package net.pladema.clinichistory.requests.medicationrequests.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.medicationrequests.application.port.output.SnomedCTSchemaPort;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetSnomedSctidWithPresentationFromCommercialMedicationSctid {

	private final SnomedCTSchemaPort snomedCTSchemaPort;

	public String run(String commercialSctid, Short presentationQuantity) {
		log.debug("Input parameters -> commercialSctid {}, presentationQuantity {}", commercialSctid, presentationQuantity);
		String result = snomedCTSchemaPort.getSnomedSctidWithPresentationFromCommercialMedication(commercialSctid, presentationQuantity);
		log.debug("Output -> {}", result);
		return result;
	}

}
