package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;

import java.util.List;

public interface MedicationService {

    List<MedicationBo> loadMedications(PatientInfoBo patientInfo, Long documentId, List<MedicationBo> medications);

}
