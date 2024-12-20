package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.DocumentRequestBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;

import java.util.List;

public interface CreateMedicationRequestService {

    List<DocumentRequestBo> execute(MedicationRequestBo medicationRequest);
}
