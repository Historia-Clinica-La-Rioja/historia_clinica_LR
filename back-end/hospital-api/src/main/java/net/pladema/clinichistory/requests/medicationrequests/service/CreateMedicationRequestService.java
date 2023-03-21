package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

public interface CreateMedicationRequestService {

    Long[] execute(MedicationRequestBo medicationRequest);
}
