package net.pladema.clinichistory.requests.medicationrequests.application.port.output;

public interface SnomedCTSchemaPort {

	String getSnomedSctidWithPresentationFromCommercialMedication(String commercialSctid, Short presentationQuantity);

	String getSnomedSctidWithPresentationFromGenericMedication(String genericSctid, Short presentationQuantity);

}
