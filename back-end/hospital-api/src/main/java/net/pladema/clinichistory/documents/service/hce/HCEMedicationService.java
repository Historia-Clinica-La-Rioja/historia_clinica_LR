package net.pladema.clinichistory.documents.service.hce;

import net.pladema.clinichistory.documents.service.hce.domain.HCEMedicationBo;

import java.util.List;

public interface HCEMedicationService {

    List<HCEMedicationBo> getMedication(Integer patientId);
}
