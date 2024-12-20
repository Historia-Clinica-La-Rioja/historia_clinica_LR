package net.pladema.clinichistory.requests.medicationrequests.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.application.port.output.CommercialMedicationSchemaPort;

import net.pladema.clinichistory.requests.medicationrequests.infrastructure.output.repository.CommercialMedicationSchemaRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommercialMedicationSchemaPortImpl implements CommercialMedicationSchemaPort {

	private final CommercialMedicationSchemaRepository commercialMedicationSchemaRepository;

	@Override
	public Integer getExternalProviderMedicationIdFromSnomedSctid(String snomedSctid) {
		return commercialMedicationSchemaRepository.fetchExternalProviderMedicationIdFromSnomedSctid(snomedSctid);
	}

}
