package net.pladema.clinichistory.requests.medicationrequests.repository;

import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

import java.util.List;

public interface GetMedicationRequestInfoRepository {

    List<Object[]> execute(Integer medicationRequestId);
}
