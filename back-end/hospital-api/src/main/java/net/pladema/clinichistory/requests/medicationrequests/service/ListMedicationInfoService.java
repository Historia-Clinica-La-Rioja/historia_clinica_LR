package net.pladema.clinichistory.requests.medicationrequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;

import java.util.List;

public interface ListMedicationInfoService {

    List<MedicationBo> execute(MedicationFilterBo filter);
}
