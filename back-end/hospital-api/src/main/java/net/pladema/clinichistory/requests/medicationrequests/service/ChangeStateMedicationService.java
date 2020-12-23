package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;

import java.util.List;

public interface ChangeStateMedicationService {

    void execute(PatientInfoBo patient, List<Integer> medicationsIds, String newStatusId, Short duration);
}
