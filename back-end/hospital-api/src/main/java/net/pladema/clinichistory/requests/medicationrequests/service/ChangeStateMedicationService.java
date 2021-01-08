package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;

public interface ChangeStateMedicationService {

    void execute(PatientInfoBo patient, ChangeStateMedicationRequestBo changeStateMedicationRequestBo);
}
