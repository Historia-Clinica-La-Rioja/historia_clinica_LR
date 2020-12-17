package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;

import java.util.List;

public interface ListMedicationInfoService {

    List<MedicationBo> execute(MedicationFilterBo filter);
}
