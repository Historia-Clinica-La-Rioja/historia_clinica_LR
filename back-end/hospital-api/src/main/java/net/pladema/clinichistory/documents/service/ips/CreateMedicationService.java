package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;

import java.util.List;

public interface CreateMedicationService {

    List<MedicationBo> execute(PatientInfoBo patientInfo, Long documentId, List<MedicationBo> medications);

}
