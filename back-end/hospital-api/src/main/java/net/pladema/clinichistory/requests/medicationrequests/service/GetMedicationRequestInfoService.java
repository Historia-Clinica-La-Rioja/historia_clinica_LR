package net.pladema.clinichistory.requests.medicationrequests.service;

import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;

public interface GetMedicationRequestInfoService {

    MedicationRequestBo execute(Integer medicationRequestId);
}
