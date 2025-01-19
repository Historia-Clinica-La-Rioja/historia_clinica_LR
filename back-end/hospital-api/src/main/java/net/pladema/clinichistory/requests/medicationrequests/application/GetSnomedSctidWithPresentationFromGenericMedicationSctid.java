package net.pladema.clinichistory.requests.medicationrequests.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.medicationrequests.application.port.output.SnomedCTSchemaPort;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetSnomedSctidWithPresentationFromGenericMedicationSctid {

	private final SnomedCTSchemaPort snomedCTSchemaPort;

	public String run(String genericSctid, Short presentationQuantity) {
		log.debug("Input parameters -> genericSctid {}, presentationQuantity {}", genericSctid, presentationQuantity);
		String result = snomedCTSchemaPort.getSnomedSctidWithPresentationFromGenericMedication(genericSctid, presentationQuantity);
		log.debug("Output -> {}", result);
		return result;
	}

}
