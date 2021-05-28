package net.pladema.clinichistory.documents.service.hce;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEMedicationBo;

import java.util.List;

public interface HCEMedicationService {

    List<HCEMedicationBo> getMedication(Integer patientId);
}
