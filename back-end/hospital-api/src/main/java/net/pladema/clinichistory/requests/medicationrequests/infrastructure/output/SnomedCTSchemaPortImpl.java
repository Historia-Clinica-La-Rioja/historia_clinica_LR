package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.application.port.output.SnomedCTSchemaPort;

import net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.SnomedCTRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SnomedCTSchemaPortImpl implements SnomedCTSchemaPort {

	private final SnomedCTRepository snomedCTRepository;

	@Override
	public String getSnomedSctidWithPresentationFromCommercialMedication(String commercialSctid, Short presentationQuantity) {
		return snomedCTRepository.fetchSnomedSctidWithPresentationFromCommercialMedication(commercialSctid, presentationQuantity);
	}

	@Override
	public String getSnomedSctidWithPresentationFromGenericMedication(String genericSctid, Short presentationQuantity) {
		return snomedCTRepository.fetchSnomedSctidWithPresentationFromGenericMedication(genericSctid, presentationQuantity);
	}

}
