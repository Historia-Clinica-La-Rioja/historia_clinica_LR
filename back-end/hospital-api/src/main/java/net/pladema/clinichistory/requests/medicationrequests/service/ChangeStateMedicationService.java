package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;

public interface ChangeStateMedicationService {

    void execute(Integer patientId, ChangeStateMedicationRequestBo changeStateMedicationRequestBo);
}
