package net.pladema.clinichistory.requests.medicationrequests.repository;

import java.util.List;

public interface GetMedicationRequestInfoRepository {

    List<Object[]> execute(Integer medicationRequestId);
}
