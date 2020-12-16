package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

public interface CreateMedicationRequestService {

    Integer execute(Integer institutionId, MedicationRequestBo medicationRequest);
}
