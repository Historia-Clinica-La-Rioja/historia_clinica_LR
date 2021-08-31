package net.pladema.clinichistory.requests.medicationrequests.service;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;

public interface ChangeStateMedicationService {

    void execute(PatientInfoBo patient, ChangeStateMedicationRequestBo changeStateMedicationRequestBo);
}
