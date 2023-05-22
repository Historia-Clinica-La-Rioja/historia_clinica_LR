package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.DocumentRequestBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

import java.util.List;

public interface CreateMedicationRequestService {

    List<DocumentRequestBo> execute(MedicationRequestBo medicationRequest);
}
