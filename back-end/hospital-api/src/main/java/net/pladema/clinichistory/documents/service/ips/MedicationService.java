package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;

import java.util.List;

public interface MedicationService {

    List<MedicationBo> loadMedications(Integer patientId, Long documentId, List<MedicationBo> medications);

}
