package net.pladema.clinichistory.requests.medicationrequests.application.port.output;

public interface CommercialMedicationSchemaPort {

	Integer getExternalProviderMedicationIdFromSnomedSctid(String snomedSctid);

}
