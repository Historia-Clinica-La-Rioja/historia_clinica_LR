package net.pladema.clinichistory.requests.medicationrequests.repository;

import net.pladema.clinichistory.requests.medicationrequests.repository.domain.MedicationFilterVo;

import java.util.List;

public interface ListMedicationRepository {

    List<Object[]> execute(MedicationFilterVo filter);

}
